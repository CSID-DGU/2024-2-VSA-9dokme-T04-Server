package org.vsa.server.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class QuestionDetailDto {
    QuestionDto question;
    List<CommentDto> commentList;
}
