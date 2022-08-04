package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.exceptionHandler.CustumException.*;
import com.hanghae99.finalproject.model.dto.requestDto.BoardRequestDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.*;
import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String link;
    private String explanation;
    private String imgPath;
    private String content;
    private DisclosureStatusType status;
    private BoardType boardType;
    private CategoryType category;
    private Long boardOrder;
    private Long folderId;
    private String folderName;
    private Long imageId;
    private List<ImageRequestDto> imageList = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.link = board.getLink();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.boardOrder = board.getBoardOrder();
    }

    public BoardResponseDto(Board board, Folder folder, ImageRequestDto saveImage) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.link = board.getLink();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.boardOrder = board.getBoardOrder();
        this.folderId = folder.getId();
        this.folderName = folder.getName();
        this.imageId = saveImage.getId();
        this.imageList.add(saveImage);
    }

    public BoardResponseDto(Board board, List<Image> images) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.link = board.getLink();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.boardOrder = board.getBoardOrder();
        this.folderId = board.getFolder().getId();
        this.imageId = findChoiceImage(images);
        this.imageList = entityToDto(images);
    }

    public BoardResponseDto(Board board, BoardRequestDto boardRequestDto, ImageRequestDto saveImage) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.link = board.getLink();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.boardOrder = board.getBoardOrder();
        this.folderId = boardRequestDto.getFolderId();
        this.folderName = boardRequestDto.getFolderName();
        this.imageId = saveImage.getId();
        this.imageList.add(saveImage);
    }

    private Long findChoiceImage(List<Image> images) {
        if (images.size() > 0) {
            for (Image image : images) {
                if (image.getImgPath().equals(this.imgPath)) {
                    return image.getId();
                }
            }
            throw new CustomException(ErrorCode.NOT_FIND_CHOICE_IMAGE);
        }
        return null;
    }

    private List<ImageRequestDto> entityToDto(List<Image> images) {
        List<ImageRequestDto> imageRequestDtos = new ArrayList<>();
        for (Image image : images) {
            imageRequestDtos.add(new ImageRequestDto(image));
        }
        return imageRequestDtos;
    }

}
