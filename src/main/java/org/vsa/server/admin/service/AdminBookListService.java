package org.vsa.server.admin.service;

import org.vsa.server.admin.dto.AdminBookDto;
import org.vsa.server.book.entity.Book;
import org.vsa.server.book.repository.BookRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@NoArgsConstructor
public class AdminBookListService {

    @Autowired
    BookRepository bookRepository;

    public List<AdminBookDto> getBookList(String search){

        List<Book> bookList = new ArrayList<>();

        if (search.isBlank()) {
            bookList = bookRepository.findAll();
        }else if(bookRepository.existsByTitle(search)){
            bookList = bookRepository.findByTitleContaining(search);
        }else if(bookRepository.existsByAuthor(search)){
            bookList = bookRepository.findByAuthorContaining(search);
        }

        return bookList.stream()
                .map(book -> AdminBookDto.builder()
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build())
                .collect(Collectors.toList());
    }




}
