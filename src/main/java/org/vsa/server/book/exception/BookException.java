package org.vsa.server.book.exception;

import org.vsa.server.book.message.ErrorMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public BookException(ErrorMessage errorMessage) {
        super("[BookException] : " + errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
    public HttpStatus getHttpStatus() {
        return this.errorMessage.getHttpStatus();
    }
}
