package org.vsa.server.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class QuestionDto {
    private int questionId;
    private String title;
    private String content;
    private int chapter;
    private int bookPage;
    private int commentCount;
    private LocalDateTime createdAt;
}
