package org.vsa.server.book.dto.response;

import org.vsa.server.member.dto.response.BookDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class MyPageDto {
    private ProfileDto profileDto;
    private Page<BookDto> bookList;
}
