package com.hanghae99.finalproject.model.dto.requestDto;

import lombok.*;

@Getter
@NoArgsConstructor
public class MailRequestDto {
    private String email;
    private String certification;

    public MailRequestDto(UserRequestDto userRequestDto) {
        this.email = userRequestDto.getEmail();
    }
}
