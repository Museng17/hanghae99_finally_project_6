package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.FolderAndBoardResponseDto;
import com.hanghae99.finalproject.model.dto.responseDto.FolderResponseDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.hanghae99.finalproject.util.resultType.CategoryType.ALL;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserinfoHttpRequest userinfoHttpRequest;
    private final BoardService boardService;
    private final ShareRepository shareRepository;
    private final UserRepository userRepository;

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
        Users users = userinfoHttpRequest.userFindByToken(request);

        Folder folder = findFolder(
                folderId,
                request
        );

        List<Board> removeBoardList = boardService.findAllById(folder);

        userinfoHttpRequest.userAndWriterMatches(
                folder.getUsers().getId(),
                users.getId()
        );

        for (Board board : removeBoardList) {
            board.removeFolderId();
        }

        List<Board> addBoardList = boardService.findAllById(
                folderRequestDto.getBoardList().stream()
                        .map(Board::getId)
                        .collect(Collectors.toList())
        );

        for (Board board : addBoardList) {
            board.addFolderId(folder);
        }

        folder.setBoardCnt(0L + addBoardList.size());

        List<Board> boards = boardService.findByUserId(users.getId());
        Folder addFolder = findByBasicFolder(users);
        for (Board board : boards) {
            if (!Optional.ofNullable(board.getFolder()).isPresent()) {
                board.addFolderId(addFolder);
            }
        }
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

        userinfoHttpRequest.userAndWriterMatches(
                folder.getUsers().getId(),
                userinfoHttpRequest.userFindByToken(request).getId()
        );

        boardService.statusUpdateByFolderId(folderId, folderRequestDto);
        folder.update(folderRequestDto);
    }

    @Transactional
    public Board crateBoardInFolder(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Board board = boardService.boardSave(
                boardRequestDto,
                request
        );

        Folder folder = findFolder(
                boardRequestDto.getFolderId(),
                request
        );

        board.addFolderId(folder);
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

    public void cloneFolder(Long folderId, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        Folder folder = findShareFolder(folderId, request);
        List<Board> boards = boardService.findAllById(folder);
        FolderRequestDto folderRequestDto = new FolderRequestDto(folder, boards);
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
    public void folderOrderChange(FolderAndBoardRequestDto folderAndBoardRequestDto, HttpServletRequest request) {
        List<FolderRequestDto> dbFolderList = toFolderRequestDtoList(
                folderRepository.findByUsers(
                        userinfoHttpRequest.userFindByToken(request))
        );

        for (FolderRequestDto folderRequestDto : folderAndBoardRequestDto.getFolderList()) {
            for (FolderRequestDto dbFolder : dbFolderList) {
                if (folderRequestDto.getId() == dbFolder.getId()) {
                    if (folderRequestDto.getFolderOrder() != dbFolder.getFolderOrder()) {
                        Folder folder = folderRepository.findById(folderRequestDto.getId())
                                .orElseThrow(() -> new RuntimeException("FolderService, 133 에러 발생 찾는 폴더가 없습니다."));
                        folder.updateOrder(folderRequestDto.getFolderOrder());
                    }
                }
            }
        }
    }

    private List<FolderRequestDto> toFolderRequestDtoList(List<Folder> folderList) {
        List<FolderRequestDto> folderRequestDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            folderRequestDtoList.add(new FolderRequestDto(folder));
        }
        return folderRequestDtoList;
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
    public List<Folder> moum(String keyword, HttpServletRequest request, Pageable pageable, Long userId) {
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

        return folderRepository.findByNameContaining(
                "%" + keyword + "%",
                users,
                boardService.findByFolder(findByBasicFolder(users)).size() > 0,
                disclosureStatuses,
                pageable
        ).getContent();
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
        return new FolderResponseDto(folders,foldersCnt);
    }

    private List<Long> listToId(List<Share> List) {
        return List.stream().map(Share::getId).collect(Collectors.toList());
    }
}
