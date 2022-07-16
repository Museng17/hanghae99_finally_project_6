package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.FolderResponseDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.util.*;
import com.hanghae99.finalproject.util.resultType.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hanghae99.finalproject.util.resultType.CategoryType.ALL;
import static com.hanghae99.finalproject.util.resultType.FileUploadType.BOARD;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserinfoHttpRequest userinfoHttpRequest;
    private final BoardService boardService;
    private final ShareRepository shareRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    private final BoardRepository boardRepository;

    @Transactional
    public Folder folderSave(FolderRequestDto folderRequestDto, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        user.setFolderCnt(user.getFolderCnt() + 1);
        return folderRepository.save(
                new Folder(
                        folderRequestDto,
                        user,
                        folderRepository.findFolderCount(user.getId())
                )
        );
    }

    @Transactional(readOnly = true)
    public Folder findFolder(Long folderId, HttpServletRequest request) {
        return folderRepository.findByIdAndUsersId(
                        folderId,
                        userinfoHttpRequest.userFindByToken(request).getId()
                )
                .orElseThrow(() -> new RuntimeException("FolderService 45 에러, 찾는 폴더가 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Folder> findAllFolder(List<Long> folderRequestDto, HttpServletRequest request) {
        return folderRepository.findAllByIdInAndUsersId(
                        folderRequestDto,
                        userinfoHttpRequest.userFindByToken(request).getId()
                )
                .orElseThrow(() -> new RuntimeException("FolderService 45 에러, 찾는 폴더가 없습니다."));
    }

    @Transactional
    public void boardInFolder(Long folderId, FolderRequestDto folderRequestDto, HttpServletRequest request) {

        Folder afterFolder = findFolder(folderId, request);

        List<Board> afterBoard = boardService.findAllById(
                folderRequestDto.getBoardList().stream()
                        .map(Board::getId)
                        .collect(Collectors.toList())
        );

        Long beforeBoardId = boardRepository.findFolderIdById(folderRequestDto.getBoardList().get(0).getId())
                .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        Long afterCnt = 1L;
        for (Board board : afterBoard) {
            board.addFolderId(afterFolder);
            board.updateOrder(afterFolder.getBoardCnt() + afterCnt);
            afterCnt++;
        }
        afterFolder.setBoardCnt(afterFolder.getBoardCnt() + afterBoard.size());

        Folder beforeFolder = findFolder(beforeBoardId, request);
        List<Board> beforeBoardList = boardRepository.findByFolder(beforeFolder);

        Long beforeCnt = 1L;
        for (Board folder : beforeBoardList) {
            folder.setBoardOrder(beforeCnt);
            beforeCnt++;
        }
        beforeFolder.setBoardCnt(beforeFolder.getBoardCnt() - afterBoard.size());
    }

    @Transactional
    public void folderDelete(List<FolderRequestDto> folderRequestDto, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        List<Long> longs = folderRequestDto.stream()
                .map(FolderRequestDto::getId)
                .collect(Collectors.toList());

        List<Folder> folders = findAllFolder(longs, request);

        List<Long> DbLongList = folders.stream()
                .map(Folder::getId)
                .collect(Collectors.toList());

        for (Folder folder : folders) {
            if (folder.getName().equals("무제")){
                throw new RuntimeException("무제폴더는 삭제할 수 없습니다.");
            }
            userinfoHttpRequest.userAndWriterMatches(
                    folder.getUsers().getId(),
                    users.getId()
            );
        }

        List<Board> removeBoardList = boardService.boardDeleteByFolderId(DbLongList);
        folderRepository.deleteAllById(DbLongList);
        users.setBoardCnt(users.getBoardCnt() - removeBoardList.size());
        users.setFolderCnt(users.getFolderCnt() - DbLongList.size());
    }

    @Transactional
    public void folderUpdate(Long folderId, HttpServletRequest request, FolderRequestDto folderRequestDto) {
        Folder folder = findFolder(
                folderId,
                request
        );

        if (folder.getName().equals("무제")) {
            throw new RuntimeException("무제 폴더는 이름을 수정할 수 없습니다.");
        }

        userinfoHttpRequest.userAndWriterMatches(
                folder.getUsers().getId(),
                userinfoHttpRequest.userFindByToken(request).getId()
        );

        boardService.statusUpdateByFolderId(folderId, folderRequestDto);
        folder.update(folderRequestDto);
    }

    @Transactional
    public Board crateBoardInFolder(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Folder folder = findFolder(
                boardRequestDto.getFolderId(),
                request
        );

        if (boardRequestDto.getBoardType() == BoardType.LINK) {
            boardRequestDto.ogTagToBoardRequestDto(
                    boardService.thumbnailLoad(boardRequestDto.getLink()),
                    boardRequestDto.getLink()
            );

            if (!boardRequestDto.getImgPath().equals("") && boardRequestDto.getImgPath() != null) {
                boardRequestDto.setImgPath(s3Uploader.upload(BOARD.getPath(), boardRequestDto.getImgPath()).getUrl());
            }

        } else if (boardRequestDto.getBoardType() == BoardType.MEMO) {
            boardRequestDto.setTitle(new SimpleDateFormat(DateType.YEAR_MONTH_DAY.getPattern()).format(new Date()));
        }

        Users user = userinfoHttpRequest.userFindByToken(request);
        Board board = boardRepository.save(new Board(boardRequestDto, folder.getBoardCnt() + 1, user, folder));
        folder.setBoardCnt(folder.getBoardCnt() + 1);
        user.setBoardCnt(user.getBoardCnt() + 1);
        return board;
    }

    @Transactional
    public void shareFolder(Long folderId, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        Folder folder = findShareFolder(folderId, request);
        Optional<Share> findShare = shareRepository.findByIdAndUsersId(folderId, users.getId());
        if (!findShare.isPresent()) {
            Share share = new Share(folder, users);
            shareRepository.save(share);
        }
    }

    private Folder findShareFolder(Long folderId, HttpServletRequest request) {
        return folderRepository.findByIdAndUsersIdNot(folderId, userinfoHttpRequest.userFindByToken(request).getId()).orElseThrow(()
                -> new RuntimeException("원하는 폴더를 찾지 못했습니다."));
    }

    @Transactional
    public void cloneFolder(Long folderId, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        Folder folder = findShareFolder(folderId, request);
        List<Board> boards = boardService.findAllById(folder);
        FolderRequestDto folderRequestDto = new FolderRequestDto(folder);
        Folder folder1 = new Folder(folderRequestDto, users);

        Folder folder2 = folderRepository.save(folder1);
        List<Board> boards1 = new ArrayList<>();
        for (Board board : boards) {
            boards1.add(new Board(board, users, folder2));
        }
        boardRepository.saveAll(boards1);
        users.setFolderCnt(users.getFolderCnt() + 1);
        users.setBoardCnt(users.getBoardCnt() + folder2.getBoardCnt());
    }

    @Transactional
    public void folderOrderChange(OrderRequestDto orderRequestDto, HttpServletRequest request) {
        Folder folder = folderRepository.findById(orderRequestDto.getFolderId())
                .orElseThrow(() -> new RuntimeException("없는 게시물입니다."));
        Users users = userinfoHttpRequest.userFindByToken(request);
        if (folder.getUsers().getId() != users.getId()) {
            throw new RuntimeException("폴더 생성자가 아닙니다.");
        }

        if (folder.getFolderOrder() == orderRequestDto.getAfterOrder() || users.getFolderCnt() + 1 < orderRequestDto.getAfterOrder()) {
            throw new RuntimeException("잘못된 정보입니다. 기존 order : " + folder.getFolderOrder() + " , 바꾸는 order : " + orderRequestDto.getAfterOrder() + " , forder 최종 order : " + users.getFolderCnt());
        } else if (folder.getFolderOrder() - orderRequestDto.getAfterOrder() > 0) {
            folderRepository.updateOrderSum(folder.getFolderOrder(), orderRequestDto.getAfterOrder());
        } else {
            folderRepository.updateOrderMinus(folder.getFolderOrder(), orderRequestDto.getAfterOrder());
        }

        folder.updateOrder(orderRequestDto.getAfterOrder());

    }

    @Transactional(readOnly = true)
    public List<Folder> folders() {
        return folderRepository.findAll();
    }

    @Transactional
    public Page<Folder> findBestFolder(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("sharedCount").descending());
        return folderRepository.findAllBystatus(DisclosureStatus.PUBLIC, pageRequest);
    }

    public Folder findByBasicFolder(Users users) {
        return folderRepository.findByUsersAndName(users, "무제");
    }

    @Transactional(readOnly = true)
    public List<Folder> moum(String keyword,
                             HttpServletRequest request,
                             Pageable pageable,
                             Long userId,
                             List<FolderRequestDto> folderRequestDtos) {
        List<DisclosureStatus> disclosureStatuses = new ArrayList<>();
        disclosureStatuses.add(DisclosureStatus.PUBLIC);

        Users users = userRepository.findById(userId)
                .orElseGet(() -> {
                    if (userId == 0L) {
                        disclosureStatuses.add(DisclosureStatus.PRIVATE);
                        return userinfoHttpRequest.userFindByToken(request);
                    }
                    throw new RuntimeException("회원을 찾을 수 없습니다.");
                });

        Optional<FolderRequestDto> findAllCategory = folderRequestDtos.stream()
                .filter(categoryType -> categoryType.getCategory() == ALL)
                .findFirst();

        if (findAllCategory.isPresent()) {
            return folderRepository.findByNameContaining(
                    "%" + keyword + "%",
                    users,
                    boardService.findByFolder(findByBasicFolder(users)).size() > 0,
                    disclosureStatuses,
                    pageable
            ).getContent();
        }

        return folderRepository.findByNameContaining(
                "%" + keyword + "%",
                users,
                boardService.findByFolder(findByBasicFolder(users)).size() > 0,
                disclosureStatuses,
                boardService.findSelectCategory(folderRequestDtos),
                pageable
        ).getContent();
    }

    @Transactional(readOnly = true)
    public List<Map<String, CategoryType>> findCategoryList(Long userId, HttpServletRequest request) {
        Users users = userRepository.findById(userId)
                .orElseGet(() -> {
                    if (userId == 0L) {
                        return userinfoHttpRequest.userFindByToken(request);
                    }
                    throw new RuntimeException("회원을 찾을 수 없습니다.");
                });
        return boardService.findCategoryByUsersId(users.getId());
    }

    @Transactional(readOnly = true)
    public List<Folder> shareList(String keyword, HttpServletRequest request, Pageable pageable, Long userId) {
        List<DisclosureStatus> disclosureStatuses = new ArrayList<>();
        disclosureStatuses.add(DisclosureStatus.PUBLIC);

        Users users = userRepository.findById(userId)
                .orElseGet(() -> {
                    if (userId == 0L) {
                        disclosureStatuses.add(DisclosureStatus.PRIVATE);
                        return userinfoHttpRequest.userFindByToken(request);
                    }
                    throw new RuntimeException("회원을 찾을 수 없습니다.");
                });

        return folderRepository.findAllByIdAndNameLike(
                listToId(shareRepository.findAllByUsersId(users.getId())),
                "%" + keyword + "%",
                pageable
        ).getContent();
    }

    public FolderResponseDto allFolders(String keyword, int page) {
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("createdDate").descending());
        Page<Folder> folders = folderRepository.findAllByNameContaining1(
                "%" + keyword + "%", DisclosureStatus.PUBLIC, pageRequest);
        int foldersCnt = folderRepository.findAllByNameContaining1("%" + keyword + "%", DisclosureStatus.PUBLIC).size();
        return new FolderResponseDto(folders, foldersCnt);
    }

    private List<Long> listToId(List<Share> List) {
        return List.stream().map(Share::getId).collect(Collectors.toList());
    }
}
