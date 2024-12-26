package org.vsa.server.book.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookInfoFromBarcodeResponse {
    private Long bookId; // 존재하는 경우에만 반환
    private String title;
    private String publisher;
    private String message; // 존재하지 않는 경우에만 반환
}
