package org.vsa.server.inquiring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class InquireListDto {
    private Page<InquireDto> inquireList;
}
