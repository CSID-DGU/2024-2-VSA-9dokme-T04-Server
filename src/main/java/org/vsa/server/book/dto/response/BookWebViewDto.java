package org.vsa.server.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class BookWebViewDto {
    private String title;

    private String category;

    private String author;

    private String pdfUrl;

}
