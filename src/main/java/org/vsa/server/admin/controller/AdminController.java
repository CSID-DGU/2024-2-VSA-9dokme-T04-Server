package org.vsa.server.admin.controller;

import org.vsa.server.book.dto.request.BookCreateRequest;
import org.vsa.server.book.dto.request.BookUpdateRequest;
import org.vsa.server.book.dto.response.BookInfoResponse;
import org.vsa.server.book.service.BookService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;



    // 책 정보(pdf) 등록
    @PostMapping("/books")
    public ResponseEntity<BookInfoResponse> createBook(@RequestBody BookCreateRequest request) throws MessagingException {
        BookInfoResponse response = bookService.createBook(request);


        return ResponseEntity.ok(response);
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
