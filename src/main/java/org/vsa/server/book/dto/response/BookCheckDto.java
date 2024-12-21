package org.vsa.server.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookCheckDto {

    private Long bookId;

    private String pdfImage;

    private String title;

    private String author;

    private String category;

    private String publisher;

    private String description;

    private int lastPage;

    private boolean isMarked;


}
