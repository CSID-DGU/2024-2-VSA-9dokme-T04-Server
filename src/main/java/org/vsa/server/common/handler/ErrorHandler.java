package org.vsa.server.common.handler;

import org.vsa.server.book.exception.BookException;
import org.vsa.server.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BookException.class)
    public ResponseEntity<ErrorResponse> bookException(BookException exception) {
        log.error(exception.getMessage());
        return ResponseEntity
                .status(exception.getErrorMessage().getHttpStatus())
                .body(ErrorResponse.of(exception.getMessage(), exception.getErrorMessage().getHttpStatus()));
    }
}