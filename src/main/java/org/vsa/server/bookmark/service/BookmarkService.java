package org.vsa.server.bookmark.service;

import org.vsa.server.book.entity.Book;
import org.vsa.server.book.repository.BookRepository;
import org.vsa.server.book.service.BookService;
import org.vsa.server.bookmark.entity.Bookmark;
import org.vsa.server.bookmark.exception.BookmarkException;
import org.vsa.server.bookmark.message.ErrorMessage;
import org.vsa.server.bookmark.repository.BookmarkRepository;
import org.vsa.server.bookmark.service.dto.request.BookUnMarkRequest;
import org.vsa.server.bookmark.service.dto.response.BookmarkResponse;
import org.vsa.server.member.entity.Member;
import org.vsa.server.member.repository.MemberRepository;
import org.vsa.server.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    @Autowired
    private  BookmarkRepository bookmarkRepository;
    @Autowired
    private  MemberService memberService;
    @Autowired
    private  BookService bookService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public BookmarkResponse mark(Long BookId, Long MemberId) {
//        Member member = memberService.getCurrentMember();
//        Book book = bookService.findById(request.bookId()).orElseThrow();
        Member member = memberRepository.findByMemberId(MemberId);
        Book book = bookRepository.findByBookId(BookId);

        // 이미 북마크가 존재하는지 확인
        if (bookmarkRepository.findByBookAndMember(book, member).isPresent()) {
            throw new BookmarkException(ErrorMessage.ALREADY_BOOKMARKED);
        }

        Bookmark bookmark = bookmarkRepository.save(new Bookmark(member, book));
        return BookmarkResponse.of(book.getBookId(), true);
    }

    @Transactional
    public BookmarkResponse unmark(BookUnMarkRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Book book = bookRepository.findByBookId(request.bookId());

        Bookmark bookmark = bookmarkRepository.findByBookAndMember(book, member)
                .orElseThrow(() -> new BookmarkException(ErrorMessage.NOT_FOUND_BOOKMARK));

        bookmarkRepository.delete(bookmark);
        return BookmarkResponse.of(book.getBookId(), false);
    }
}