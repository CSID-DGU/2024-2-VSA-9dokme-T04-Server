package org.vsa.server.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookDto {
    private Long bookId;
    private String title;
    private String category;
    private String bookImage;
    private String bookUrl;
    private Boolean isMarked;
}