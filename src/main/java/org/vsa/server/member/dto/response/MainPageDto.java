package org.vsa.server.member.dto.response;

import org.vsa.server.book.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class MainPageDto {

    private List<Advertisement> advertisementList;
    private Page<BookDto> bookList;

}
