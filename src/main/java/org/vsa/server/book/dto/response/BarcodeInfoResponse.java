package org.vsa.server.book.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarcodeInfoResponse {

    @JsonProperty("item")
    private Item[] items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String title;        // 책 제목
        private String publisher;    // 출판사
    }
}
