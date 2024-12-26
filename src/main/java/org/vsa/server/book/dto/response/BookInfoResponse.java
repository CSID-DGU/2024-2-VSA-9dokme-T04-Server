package org.vsa.server.book.dto.response;

import java.time.LocalDateTime;


public record BookInfoResponse(
        Long bookId,
        String title,
        LocalDateTime publishDate,
        String author,
        String publisher,
        String category,
        String description,
        String bookImage,
        String bookURL,
        int bookChapter,
        int bookFullPage,
        Integer rent
) {}