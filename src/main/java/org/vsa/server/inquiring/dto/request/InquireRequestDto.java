package org.vsa.server.inquiring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InquireRequestDto {
    private String title;
    private String content;
//    private long userId;
}
