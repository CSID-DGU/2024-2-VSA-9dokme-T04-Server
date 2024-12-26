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
import org.vsa.server.book.dto.response.BookInfoFromBarcodeResponse;
import org.vsa.server.book.entity.Book;
import org.vsa.server.book.service.AladinService;
import org.vsa.server.book.service.BarcodeService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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

            // DB에서 책 존재 여부 확인
            Optional<Book> existingBook = barcodeService.findByTitleAndPublisher(bookInfo.getTitle(), bookInfo.getPublisher());

            if (existingBook.isPresent()) {
                // 책이 존재할 경우 bookId, title, publisher 반환
                Book book = existingBook.get();
                BookInfoFromBarcodeResponse response = BookInfoFromBarcodeResponse.builder()
                        .bookId(book.getBookId())
                        .title(book.getTitle())
                        .publisher(book.getPublisher())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                // 책이 존재하지 않을 경우 title, publisher, 메시지 반환
                BookInfoFromBarcodeResponse response = BookInfoFromBarcodeResponse.builder()
                        .title(bookInfo.getTitle())
                        .publisher(bookInfo.getPublisher())
                        .message("해당 도서는 데이터베이스에 존재하지 않습니다.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the uploaded file");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}
