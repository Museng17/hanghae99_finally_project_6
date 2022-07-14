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
                        boardRepository.findBoardCount(user.getId()),
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
    public void boardDelete(List<BoardRequestDto> boardRequestDto, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        List<Long> longs = boardRequestDto.stream()
                .map(BoardRequestDto::getId)
                .collect(Collectors.toList());

        List<Board> boardList = boardAllFindById(longs);
        for (Board board : boardList) {
            userinfoHttpRequest.userAndWriterMatches(
                    board.getUsers().getId(),
                    users.getId()
            );
        }

        boardRepository.deleteAllById(longs);
        users.setBoardCnt(users.getBoardCnt() - boardList.size());
    }

    public Board boardFindById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BoardService 74 에러, 해당 글을 찾지 못했습니다."));
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
    public void boardOrderChange(FolderAndBoardRequestDto folderAndBoardRequestDto, HttpServletRequest request) {
        List<BoardRequestDto> dbList = toBoardRequestDtoList(
                boardRepository.findByUsers(
                        userinfoHttpRequest.userFindByToken(request)
                )
        );

        for (BoardRequestDto boardRequestDto : folderAndBoardRequestDto.getBoardList()) {
            for (BoardRequestDto dbDto : dbList) {
                if (boardRequestDto.getId() == dbDto.getId()) {
                    if (boardRequestDto.getBoardOrder() != dbDto.getBoardOrder()) {
                        Board targetBoard = boardFindById(boardRequestDto.getId());
                        targetBoard.updateOrder(boardRequestDto.getBoardOrder());
                    }
                }
            }
        }
    }

    private List<BoardRequestDto> toBoardRequestDtoList(List<Board> boards) {
        List<BoardRequestDto> boardRequestDtoList = new ArrayList<>();

        for (Board board : boards) {
            boardRequestDtoList.add(new BoardRequestDto(board));
        }
        return boardRequestDtoList;
    }

    public FileUploadResponse boardImageUpload(MultipartFile imageFile) {
        return s3Uploader.upload(imageFile, BOARD.getPath());
    }

    @Transactional
    public Page<Board> findNewBoard(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate"));
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

    private List<CategoryType> findSelectCategory(List<FolderRequestDto> folderRequestDtos) {
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
