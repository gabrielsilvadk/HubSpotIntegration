package com.example.hubspotintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubSpotAuthResponseDTO {
    private String access_token;
    private String refresh_token;
    private Long expires_in;
    private String token_type;
}
