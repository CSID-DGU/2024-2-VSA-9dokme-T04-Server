package org.vsa.server.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.vsa.server.book.dto.request.BookCreateRequest;
import org.vsa.server.book.dto.request.BookUpdateRequest;
import org.vsa.server.book.dto.response.BookInfoResponse;
import org.vsa.server.book.service.BookService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private BookService bookService;



    // 책 정보(pdf) 등록
    @PostMapping("/books")
    public ResponseEntity<BookInfoResponse> createBook(
            @RequestParam("title") String title,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("bookImage") MultipartFile bookImage,
            @RequestParam("bookPDF") MultipartFile bookPDF
    ) throws IOException, MessagingException {

        try {
            // Step 1: publishDate를 LocalDateTime으로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            LocalDateTime dateTime = LocalDateTime.parse(publishDate, formatter);

            // Step 2: 책 정보 저장
            BookCreateRequest request = new BookCreateRequest(
                    title, dateTime, author, publisher, category, description,
                    null, null, 0, 0, 0
            );
            BookInfoResponse response = bookService.createBook(request);
            Long bookId = response.bookId(); // 저장된 책 ID 가져오기

            // Step 3: 파일 저장 경로 설정 (프로젝트 루트 하위)
            String baseDir = new File("files").getAbsolutePath();
            String imagePath = baseDir + "/cover/" + bookId + ".png";
            String pdfPath = baseDir + "/pdf/" + bookId + ".pdf";

            System.out.println("이미지 저장 경로: " + imagePath);
            System.out.println("PDF 저장 경로: " + pdfPath);

            // Step 4: 파일 저장
            File imageFile = new File(imagePath);
            File pdfFile = new File(pdfPath);

            if (!imageFile.getParentFile().exists()) {
                boolean isImageDirCreated = imageFile.getParentFile().mkdirs();
                System.out.println("이미지 디렉토리 생성 여부: " + isImageDirCreated);
            }

            if (!pdfFile.getParentFile().exists()) {
                boolean isPdfDirCreated = pdfFile.getParentFile().mkdirs();
                System.out.println("PDF 디렉토리 생성 여부: " + isPdfDirCreated);
            }

            bookImage.transferTo(imageFile);
            System.out.println("이미지 파일 저장 성공: " + imageFile.getAbsolutePath());

            bookPDF.transferTo(pdfFile);
            System.out.println("PDF 파일 저장 성공: " + pdfFile.getAbsolutePath());

            // Step 5: 책 엔티티 업데이트 (파일 경로 저장)
            bookService.updateBookFilePaths(bookId, "/cover/" + bookId + ".png", "/pdf/" + bookId + ".pdf");

            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            System.err.println("날짜 형식 변환 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            System.err.println("파일 저장 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





    // 책 정보(pdf) 수정
    @PutMapping("/books/{bookId}")
    public ResponseEntity<BookInfoResponse> updateBook(@PathVariable Long bookId, @RequestBody BookUpdateRequest request) {
        BookInfoResponse response = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(response);
    }

    // 책 정보(pdf) 삭제
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
