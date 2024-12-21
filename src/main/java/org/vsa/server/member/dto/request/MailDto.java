package org.vsa.server.member.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class MailDto {
    private String[] emailAddr;	// 수신 이메일
    private String emailTitle; // 메일 제목
    private String emailContent;// 메일 내용
}