package org.vsa.server.book.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    INVALID_BOOK_ID(NOT_FOUND, "유효하지 않은 책 id 입니다."),
    NOT_FOUND_BOOK(NOT_FOUND, "해당하는 책이 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}