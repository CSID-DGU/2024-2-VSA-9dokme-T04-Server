package org.vsa.server.book.dto.request;

import java.time.LocalDateTime;


public record BookUpdateRequest(
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