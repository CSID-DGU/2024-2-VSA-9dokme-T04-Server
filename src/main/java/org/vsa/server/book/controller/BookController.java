package org.vsa.server.book.controller;

import org.vsa.server.book.dto.response.BookCheckDto;
import org.vsa.server.book.dto.response.BookWebViewDto;
import org.vsa.server.book.dto.response.MyPageDto;
import org.vsa.server.book.repository.BookRepository;
import org.vsa.server.book.service.BookService;
import org.vsa.server.common.FindLoginMember;
import org.vsa.server.member.dto.response.BookDto;
import org.vsa.server.member.entity.Keyword;
import org.vsa.server.member.entity.Member;
import org.vsa.server.member.repository.KeywordRepository;
import org.vsa.server.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private KeywordRepository keywordRepository;

    @GetMapping("/books")
    @Operation(summary = "pdf 교재 상세조회", description = "pdf 교재 상세조회")
    public ResponseEntity<BookCheckDto> getBookDetail(@RequestParam Long id) {

        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();

        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 후 이용해주세요.");
        }
        if(bookRepository.existsById(id)==false){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 pdf 교재입니다.");

        }

        BookCheckDto dto = bookService.checkBook(id,memberId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/mainpage/search")
    @Operation(summary = "pdf 교재 검색", description = "pdf 교재 검색 title 기반")
    public ResponseEntity<Page<BookDto>> searchBookPDF(@RequestParam(defaultValue = "") String title,
                                                       @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {

        String memberEmail = FindLoginMember.getCurrentUserId();
        Member member = memberRepository.findBySocialId(memberEmail);
        Long memberId = member.getMemberId();

        Page<BookDto> dto = bookService.searchBook(title,pageNo, memberId);

        if (memberEmail == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 후 이용해주세요.");
        }
        if(dto.isEmpty()==true){
            Keyword keyword = new Keyword();
            keyword.setKeyword(title);
            keyword.setMember(member);
            keywordRepository.save(keyword);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 pdf 교재입니다.");

        }


        return ResponseEntity.ok(dto);
    }

    @Transactional
    @GetMapping("/view")
    @Operation(summary = "pdf 교재 웹뷰 조회", description = "pdf 교재 웹뷰 조회")
    public ResponseEntity<BookWebViewDto> viewBookPDF(@RequestParam Long bookId) {

        String memberEmail = FindLoginMember.getCurrentUserId();
        Member currentMember = memberRepository.findBySocialId(memberEmail);


        if (currentMember == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 후 이용해주세요.");
        }

        if(bookRepository.existsById(bookId)==false){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 pdf 교재입니다.");
        }

        BookWebViewDto dto = bookService.bookWebView(bookId);

        bookService.saveRentBook(bookId,memberEmail);

        return ResponseEntity.ok(dto);
    }

    @Transactional
    @PutMapping("/view/{bookId}")
    @Operation(summary = "웹 뷰 조회 나가기", description = "웹 뷰 조회를 나가며 진행률 최신화(뒤로가기)")
    public ResponseEntity<String> quitViewBookPDF(@PathVariable Long bookId,
                                            @RequestParam int lastPage){

        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();


        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 후 이용해주세요.");
        }

        bookService.updateProgress(bookId,memberId,lastPage);

        return ResponseEntity.ok("웹 뷰 종료");
    }

    @GetMapping("/mypage")
    public MyPageDto mypage(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo){

        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();

        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 후 이용해주세요.");
        }

        return bookService.getMypageBookList(memberId, pageNo);
    }

//    @GetMapping("/mypage")
//    @Operation(summary = "마이페이지", description = "마이페이지,세션기반")
//    public MyPageDto mypage(HttpServletRequest request,
//                            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo){
//
//        HttpSession session = request.getSession();
//
//        String socialId = session.getAttribute("email").toString();
//
//        return bookService.getMypageBookList(Long.valueOf(socialId), pageNo);
//    }
}

