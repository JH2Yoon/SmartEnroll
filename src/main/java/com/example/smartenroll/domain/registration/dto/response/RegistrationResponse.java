package com.example.smartenroll.domain.registration.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationResponse {

    private Long registrationId;
    private String courseCode;
    private String title;
}