package com.example.smartenroll.common.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
}