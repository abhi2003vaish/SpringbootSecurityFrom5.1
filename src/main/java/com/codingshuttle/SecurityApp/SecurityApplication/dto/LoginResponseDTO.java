package com.codingshuttle.SecurityApp.SecurityApplication.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDTO {

    private Long id;
    private String AccessToken;
    private String RefreshToken;
}
