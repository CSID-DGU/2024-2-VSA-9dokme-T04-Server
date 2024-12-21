package org.vsa.server.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/* 추가! */
@Data
public class KakaoTokenDto {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;
}
