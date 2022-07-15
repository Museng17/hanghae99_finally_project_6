package com.hanghae99.finalproject.service;

import com.amazonaws.services.s3.model.S3Object;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.util.*;
import com.hanghae99.finalproject.util.resultType.*;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hanghae99.finalproject.util.resultType.CategoryType.*;
import static com.hanghae99.finalproject.util.resultType.FileUploadType.BOARD;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FolderRepository folderRepository;
    private final UserinfoHttpRequest userinfoHttpRequest;
    private final ShareRepository shareRepository;
    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;

    @Transactional
    public Board boardSave(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        if (boardRequestDto.getBoardType() == BoardType.LINK) {
            boardRequestDto.ogTagToBoardRequestDto(
                    thumbnailLoad(boardRequestDto.getLink()),
                    boardRequestDto.getLink()
            );

            if (!boardRequestDto.getImgPath().equals("") && boardRequestDto.getImgPath() != null) {
                boardRequestDto.setImgPath(s3Uploader.upload(BOARD.getPath(), boardRequestDto.getImgPath()).getUrl());
            }

        } else if (boardRequestDto.getBoardType() == BoardType.MEMO) {
            boardRequestDto.setTitle(new SimpleDateFormat(DateType.YEAR_MONTH_DAY.getPattern()).format(new Date()));
        }

        Users user = userinfoHttpRequest.userFindByToken(request);

        Folder folder = folderRepository.findByUsersAndName(user, "무제");

        Board board = boardRepository.save(
                new Board(
                        folder.getBoardCnt(),
                        boardRequestDto,
                        user,
                        folder
                )
        );
        folder.setBoardCnt(folder.getBoardCnt() + 1);
        user.setBoardCnt(user.getBoardCnt() + 1);
        return board;
    }

    @Transactional
    public void boardUpdate(Long id, BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Board board = boardFindById(id);

        userinfoHttpRequest.userAndWriterMatches(
                board.getUsers().getId(),
                userinfoHttpRequest.userFindByToken(request).getId()
        );
        if (boardRequestDto.getBoardType() == BoardType.LINK) {
            if (!board.getImgPath().equals(boardRequestDto.getImgPath())) {
                S3Object removeImageUrl = s3Uploader.selectImage(BOARD.getPath(), board.getImgPath());
                if (Optional.ofNullable(removeImageUrl).isPresent()) {
                    s3Uploader.fileDelete(removeImageUrl.getKey());
                }
                S3Object addImageUrl = s3Uploader.selectImage(BOARD.getPath(), boardRequestDto.getImgPath());
                if (!Optional.ofNullable(addImageUrl).isPresent()) {
                    boardRequestDto.setImgPath(s3Uploader.upload(BOARD.getPath(), boardRequestDto.getImgPath()).getUrl());
                }
            }
        }

        board.update(boardRequestDto);
    }

    @Transactional
    public void boardDelete(List<BoardRequestDto> boardRequestDto, HttpServletRequest request, Long folderId) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        List<Long> longs = boardRequestDto.stream()
                .map(BoardRequestDto::getId)
                .collect(Collectors.toList());

        List<Board> boardList = boardAllFindById(longs);
        for (Board board : boardList) {
            userinfoHttpRequest.userAndWriterMatches(board.getUsers().getId(), users.getId());
        }

        boardRepository.deleteAllById(longs);
        users.setBoardCnt(users.getBoardCnt() - boardList.size());

        List<Board> removeAfterBoardList = boardFindByUsersIdOrderByBoardOrderAsc(users.getId(), folderId);

        Long cnt = 1L;
        for (Board board : removeAfterBoardList) {
            board.setBoardOrder(cnt);
            cnt++;
        }
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("찾는 폴더가 없습니다."));

        folder.setBoardCnt(folder.getBoardCnt() - longs.size());
    }

    public Board boardFindById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BoardService 74 에러, 해당 글을 찾지 못했습니다."));
    }

    public List<Board> boardFindByUsersIdOrderByBoardOrderAsc(Long userId, Long folderId) {
        return boardRepository.findAllByUsersIdOrderByBoardOrderAsc(userId, folderId);
    }

    public List<Board> boardAllFindById(List<Long> boardIdList) {
        return boardRepository.findAllById(boardIdList);

    }

    public List<Board> findAllById(List<Long> longs) {
        return boardRepository.findAllById(longs);
    }

    public List<Board> findAllById(Folder folder) {
        return boardRepository.findByFolder(folder);
    }

    public List<Board> boardDeleteByFolderId(List<Long> folderIdList) {
        return boardRepository.deleteAllByFolderIdIn(folderIdList);
    }

    public void statusUpdateByFolderId(Long id, FolderRequestDto folderRequestDto) {
        Optional<Board> board = boardRepository.findByFolderId(id);

        if (board.isPresent()) {
            board.get().updateStatus(folderRequestDto);
        }

    }

    public OgResponseDto thumbnailLoad(String url) {
        OgResponseDto ogResponseDto = new OgResponseDto();
        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.select("meta[property=og:title]").attr("content");
            String image = doc.select("meta[property=og:image]").attr("content");
            String description = doc.select("meta[property=og:description]").attr("content");
            ogResponseDto = new OgResponseDto(title, image, description);
        } catch (Exception e) {
            ogResponseDto = new OgResponseDto("", "", "");
        }
        return ogResponseDto;

    }

    public Board findShareBoard(Long boardId, HttpServletRequest request) {
        return boardRepository.findByIdAndUsersIdNot(boardId, userinfoHttpRequest.userFindByToken(request).getId()).orElseThrow(()
                -> new RuntimeException("원하는 폴더를 찾지 못했습니다."));
    }

    public void cloneBoard(Long boardId, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        Board board = findShareBoard(boardId, request);
        BoardRequestDto boardRequestDto = new BoardRequestDto(board);
        Board board1 = new Board(boardRequestDto, users);
        boardRepository.save(board1);
        users.setBoardCnt(users.getBoardCnt() + 1);
    }

    @Transactional
    public void boardOrderChange(OrderRequestDto orderRequestDto, HttpServletRequest request) {
        Board board = boardRepository.findById(orderRequestDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("없는 게시물입니다."));
        Users users = userinfoHttpRequest.userFindByToken(request);

        if (board.getUsers().getId() != users.getId()) {
            throw new RuntimeException("글쓴이가 아닙니다.");
        }

        if (board.getBoardOrder() == orderRequestDto.getAfterOrder() || orderRequestDto.getAfterOrder() > users.getBoardCnt()) {
            throw new RuntimeException("잘못된 정보입니다. : 기존 order : " + board.getBoardOrder() + "바꾸는 order : " + orderRequestDto.getAfterOrder() + "forder 최종 order : " + users.getBoardCnt() + 1);
        } else if (board.getBoardOrder() - orderRequestDto.getAfterOrder() > 0) {
            boardRepository.updateOrderSum(board.getBoardOrder(), orderRequestDto.getAfterOrder(), orderRequestDto.getFolderId());
        } else {
            boardRepository.updateOrderMinus(board.getBoardOrder(), orderRequestDto.getAfterOrder(), orderRequestDto.getFolderId());
        }

        board.updateOrder(orderRequestDto.getAfterOrder());
    }

    public FileUploadResponse boardImageUpload(MultipartFile imageFile) {
        return s3Uploader.upload(imageFile, BOARD.getPath());
    }

    @Transactional
    public Page<Board> findNewBoard(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return boardRepository.findAllByStatus(DisclosureStatus.PUBLIC, pageRequest);
    }

    public List<CategoryType> FolderRequestDtoToCategoryTypeList(List<FolderRequestDto> folderRequestDtos) {
        List<CategoryType> categoryTypeList = new ArrayList<>();
        for (FolderRequestDto folderRequestDto : folderRequestDtos) {
            categoryTypeList.add(folderRequestDto.getCategory());
        }
        return categoryTypeList;
    }

    public List<Board> findByFolder(Folder folder) {
        return boardRepository.findByFolder(folder);
    }

    public List<Board> findByUserId(Long id) {
        return boardRepository.findByUsersId(id);
    }

    @Transactional(readOnly = true)
    public FolderRequestDto moum(List<FolderRequestDto> folderRequestDtos,
                                 String keyword,
                                 HttpServletRequest request,
                                 Pageable pageable,
                                 Long folderId,
                                 Long userId) {
        List<DisclosureStatus> disclosureStatuses = new ArrayList<>();
        disclosureStatuses.add(DisclosureStatus.PUBLIC);

        Users user = userRepository.findById(userId)
                .orElseGet(() -> {
                    if (userId == 0L) {
                        disclosureStatuses.add(DisclosureStatus.PRIVATE);
                        return userinfoHttpRequest.userFindByToken(request);
                    }
                    throw new RuntimeException("회원을 찾을 수 없습니다.");
                });

        return new FolderRequestDto(
                boardRepository.findByFolderIdAndTitleContainingAndCategoryIn(
                        folderId,
                        "%" + keyword + "%",
                        findSelectCategory(folderRequestDtos),
                        user.getId(),
                        disclosureStatuses,
                        pageable
                ),
                folderRepository.findById(folderId).get()
        );
    }

    public BoardResponseDto allBoards(String keyword, int page, List<FolderRequestDto> folderRequestDtos) {
        Optional<FolderRequestDto> all = folderRequestDtos.stream()
                .filter(categoryType -> categoryType.getCategory() == ALL)
                .findFirst();
        int boardsCnt;
        Page<Board> boards;
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("createdDate").descending());
        if (all.isPresent()) {
            boards = boardRepository.findAllByStatusAndTitleContaining(DisclosureStatus.PUBLIC, "%" + keyword + "%", pageRequest);
            boardsCnt = boardRepository.findAllByStatusAndTitleContaining(DisclosureStatus.PUBLIC, "%" + keyword + "%").size();
        } else {
            boards = boardRepository.findAllByStatusAndTitleContainingAndCategoryIn(DisclosureStatus.PUBLIC, "%" + keyword + "%", FolderRequestDtoToCategoryTypeList(folderRequestDtos), pageRequest);
            boardsCnt = boardRepository.findAllByStatusAndTitleContainingAndCategoryIn(DisclosureStatus.PUBLIC, "%" + keyword + "%", FolderRequestDtoToCategoryTypeList(folderRequestDtos))
                    .size();
        }
        return new BoardResponseDto(boards, boardsCnt);
    }

    @Transactional(readOnly = true)
    public List<Map<String, CategoryType>> findCategoryList(Long userId, Long folderId, HttpServletRequest request) {
        Users user = userRepository.findById(userId)
                .orElseGet(() -> {
                    if (userId == 0L) {
                        return userinfoHttpRequest.userFindByToken(request);
                    }
                    throw new RuntimeException("회원을 찾을 수 없습니다.");
                });

        return findCategoryByUsersIdAndFolderId(user.getId(), folderId);
    }

    private List<Map<String, CategoryType>> findCategoryByUsersIdAndFolderId(Long id, Long folderId) {
        return ListToListMap(boardRepository.findCategoryByUsersIdAndFolderId(id, folderId));
    }

    public List<Map<String, CategoryType>> findCategoryByUsersId(Long id) {
        return ListToListMap(boardRepository.findAllCategoryByUsersId(id));
    }

    public List<Map<String, CategoryType>> ListToListMap(List<CategoryType> categoryList) {
        List<Map<String, CategoryType>> addList = new ArrayList<>();
        for (CategoryType categoryType : categoryList) {
            if (categoryType != CategoryType.NO_CATEGORY) {
                Map<String, CategoryType> adMap = new HashMap<>();
                adMap.put("category", categoryType);
                addList.add(adMap);
            }
        }
        return addList;
    }

    public List<CategoryType> findSelectCategory(List<FolderRequestDto> folderRequestDtos) {
        List<CategoryType> categoryTypeList = null;

        Optional<FolderRequestDto> findAllCategory = folderRequestDtos.stream()
                .filter(categoryType -> categoryType.getCategory() == ALL)
                .findFirst();

        if (findAllCategory.isPresent()) {
            categoryTypeList = ALL_CATEGORYT;
        } else {
            categoryTypeList = FolderRequestDtoToCategoryTypeList(folderRequestDtos);
        }
        return categoryTypeList;
    }
}
