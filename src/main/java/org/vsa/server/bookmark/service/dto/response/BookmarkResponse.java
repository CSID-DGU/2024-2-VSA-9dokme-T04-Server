package org.vsa.server.bookmark.service.dto.response;

public record BookmarkResponse(Long bookmarkId, boolean isMarked) {

    public static BookmarkResponse of(Long bookId, boolean isMarked) {
        return new BookmarkResponse(bookId, isMarked);
    }
}