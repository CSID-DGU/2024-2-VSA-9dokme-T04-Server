package org.vsa.server.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostWrittenDto {
    private int questionId;
    private Long bookId;
    private String title;
    private String content;
    private int commentCount;
    private int chapter;
    private int page;
}
