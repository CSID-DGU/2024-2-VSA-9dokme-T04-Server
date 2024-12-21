package org.vsa.server.inquiring.exception;

import org.vsa.server.inquiring.message.ErrorMessage;
import lombok.Getter;

@Getter
public class InquireException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public InquireException(ErrorMessage errorMessage) {
        super("[InquiringException] : " + errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}