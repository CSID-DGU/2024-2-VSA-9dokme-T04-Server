package org.vsa.server.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorResponse(
        boolean success,
        @NonNull String message,
        HttpStatus status
) implements BaseResponse {

    public static ErrorResponse of(String message, HttpStatus status) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .status(status)
                .build();
    }
}