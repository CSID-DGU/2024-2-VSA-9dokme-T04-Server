package org.vsa.server.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

//닉네임 추가 필요!!
@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    int commentId;
    String content;
    private LocalDateTime createdAt;
    String nickName;
    Long memberId;
}
