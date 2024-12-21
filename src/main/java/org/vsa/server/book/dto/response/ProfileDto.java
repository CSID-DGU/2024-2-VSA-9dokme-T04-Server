package org.vsa.server.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDto {
    private Long memberId;
    private String nickName;
    private String expirationDate;
    private boolean subscribed;
//    private String profileImageURL;
}
