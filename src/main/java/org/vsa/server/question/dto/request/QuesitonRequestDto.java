package org.vsa.server.question.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class QuesitonRequestDto {
    private Long bookId;
    private String chapter;
    private int bookPage;
    private int pageNo;
}
