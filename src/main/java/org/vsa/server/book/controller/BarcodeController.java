package org.vsa.server.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vsa.server.book.dto.response.BarcodeInfoResponse;
import org.vsa.server.book.service.AladinService;
import org.vsa.server.book.service.BarcodeService;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

    private final BarcodeService barcodeService;
    private final AladinService aladinService;

    @PostMapping("/extract")
    public ResponseEntity<?> extractBarcode(@RequestParam("file") MultipartFile file) {
        try {
            // MultipartFile을 로컬 임시 파일로 변환
            File tempFile = File.createTempFile("uploaded", ".png");
            file.transferTo(tempFile);

            // 바코드 추출
            String isbn = barcodeService.extractISBN(tempFile.getAbsolutePath());

            // 알라딘 API 호출로 책 정보 가져오기
            BarcodeInfoResponse.Item bookInfo = aladinService.getBarcodeInfoByISBN(isbn);

            // 임시 파일 삭제
            tempFile.delete();

            return ResponseEntity.ok(bookInfo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the uploaded file");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}
