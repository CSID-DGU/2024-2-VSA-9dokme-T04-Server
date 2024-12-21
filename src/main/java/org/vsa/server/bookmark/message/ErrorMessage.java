package org.vsa.server.bookmark.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    NOT_FOUND_BOOKMARK(NOT_FOUND, "해당하는 북마크가 존재하지 않습니다."),
    ALREADY_BOOKMARKED(CONFLICT, "이미 북마크가 존재합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}