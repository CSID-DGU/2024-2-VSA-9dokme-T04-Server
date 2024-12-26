package org.vsa.server.book.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record BarcodeRequest(
        MultipartFile image
) {
}
