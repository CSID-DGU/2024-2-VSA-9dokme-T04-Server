package org.vsa.server.bookmark.controller;

import org.vsa.server.bookmark.service.BookmarkService;
import org.vsa.server.bookmark.service.dto.request.BookUnMarkRequest;
import org.vsa.server.bookmark.service.dto.response.BookmarkResponse;
import org.vsa.server.common.FindLoginMember;
import org.vsa.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private MemberRepository memberRepository;

    // 게시글 북마크 추가
    @PostMapping
    public BookmarkResponse addBookmark(@RequestParam Long BookId) {
        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();

        return bookmarkService.mark(BookId, memberId);
    }

    // 게시글 북마크 취소
    @DeleteMapping
    public BookmarkResponse removeBookmark(@RequestParam Long bookId) {

        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();

        return bookmarkService.unmark(new BookUnMarkRequest(bookId, memberId));
    }
}
