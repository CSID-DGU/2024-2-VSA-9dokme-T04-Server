package org.vsa.server.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long MemberId;
    private String nickname;
    private String socialId;
    private String expiredDate;
}
