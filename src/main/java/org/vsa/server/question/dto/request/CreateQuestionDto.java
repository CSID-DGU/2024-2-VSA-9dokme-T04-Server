package org.vsa.server.question.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionDto {

    private String userEmail;

    // [merge] chapter String으로 변경
    private int bookChapter;

    private int bookPage;

    private String title;

    private String content;

}
