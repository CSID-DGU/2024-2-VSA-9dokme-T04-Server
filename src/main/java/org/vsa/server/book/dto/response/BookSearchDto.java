package org.vsa.server.book.dto.response;

import org.vsa.server.member.dto.response.BookDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Data
@Builder
public class BookSearchDto{
    private Page<BookDto> bookList;
}
