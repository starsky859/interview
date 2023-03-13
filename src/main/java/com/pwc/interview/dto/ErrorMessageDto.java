package com.pwc.interview.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorMessageDto {

    private final LocalDateTime timestamp;
    private final String message;

}
