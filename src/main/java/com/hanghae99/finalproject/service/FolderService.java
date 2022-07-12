package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.FolderAndBoardResponseDto;
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
    public void folderSave(FolderRequestDto folderRequestDto, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        folderRepository.save(
                new Folder(
                        folderRequestDto,
                        user,
                        folderRepository.findFolderCount(user.getId())
                )
        );
        user.setFolderCnt(user.getFolderCnt() + 1);
    }

    @Transactional(readOnly = true)
    public Folder findFolder(Long folderId, HttpServletRequest request) {
        return folderRepository.findByIdAndUsersId(
                        folderId,
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
    public void folderDelete(Long folderId, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        Folder folder = findFolder(folderId, request);

        userinfoHttpRequest.userAndWriterMatches(
                folder.getUsers().getId(),
                users.getId()
        );

        boardService.boardDeleteByFolderId(folderId);
        folderRepository.deleteById(folderId);
        users.setFolderCnt(users.getFolderCnt() - 1);
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
    public void crateBoardInFolder(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Board board = boardService.boardSave(
                boardRequestDto,
                request
        );

        Folder folder = findFolder(
                boardRequestDto.getFolderId(),
                request
        );

        board.addFolderId(folder);
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

    public Folder findShareFolder(Long folderId, HttpServletRequest request) {
        return folderRepository.findByIdAndUsersIdNot(folderId, userinfoHttpRequest.userFindByToken(request).getId()).orElseThrow(()
                -> new RuntimeException("원하는 폴더를 찾지 못했습니다."));
    }

    public void cloneFolder(Long folderId, HttpServletRequest request) {
        Users users = userinfoHttpRequest.userFindByToken(request);
        Folder folder = findShareFolder(folderId, request);
        FolderRequestDto folderRequestDto = new FolderRequestDto(folder);
        Folder folder1 = new Folder(folderRequestDto, users);
        folderRepository.save(folder1);
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

        Folder folder = findByBasicFolder(users);
        boolean isBoardInBasicFolder = false;

        if (boardService.findByFolder(folder).size() > 0) {
            isBoardInBasicFolder = true;
        }

        return folderRepository.findByNameContaining(
                "%" + keyword + "%",
                users,
                isBoardInBasicFolder,
                disclosureStatuses,
                pageable
        ).getContent();
    }

    public FolderAndBoardResponseDto allmoum(String keyword, int page, List<FolderRequestDto> folderRequestDtos) {
        Optional<FolderRequestDto> all = folderRequestDtos.stream()
                .filter(categoryType -> categoryType.getCategory() == ALL)
                .findFirst();
        Page<Board> boards;
        PageRequest pageRequest = PageRequest.of(page, 4, Sort.by("createdDate").descending());
        if (all.isPresent()) {
            boards = boardRepository.findAllByStatusAndTitleContaining(DisclosureStatus.PUBLIC, "%" + keyword + "%", pageRequest);
        } else {
            boards = boardRepository.findAllByStatusAndTitleContainingAndCategoryIn(DisclosureStatus.PUBLIC, "%" + keyword + "%", boardService.FolderRequestDtoToCategoryTypeList(folderRequestDtos), pageRequest);
        }
        Page<Folder> folders = folderRepository.findAllByNameContaining1(
                "%" + keyword + "%", DisclosureStatus.PUBLIC, pageRequest);
        return new FolderAndBoardResponseDto(boards, folders);
    }
}
