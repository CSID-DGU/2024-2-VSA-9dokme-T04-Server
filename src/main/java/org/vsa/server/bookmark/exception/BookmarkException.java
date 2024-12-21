package org.vsa.server.bookmark.exception;

import org.vsa.server.bookmark.message.ErrorMessage;
import lombok.Getter;

@Getter
public class BookmarkException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public BookmarkException(ErrorMessage errorMessage) {
        super("[BookmarkException] : " + errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
