package org.vsa.server.gpt.dto;

import lombok.Data;

@Data
public class PdfChatRequest {
    private String url;
    private String question;
    private String bookName;
}
