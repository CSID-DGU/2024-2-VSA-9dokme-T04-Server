package org.vsa.server.bookmark.repository;

import org.vsa.server.book.entity.Book;
import org.vsa.server.bookmark.entity.Bookmark;
import org.vsa.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Long countByBook(Book book);  // Book 엔티티 전체를 기반으로 카운트
    List<Bookmark> findByMember(Member member);  // Member를 기준으로 북마크 찾기
    Optional<Bookmark> findByBookAndMember(Book book, Member member);


    boolean existsBookmarkByBook_BookIdAndMember_MemberId(Long bookId, Long memberId);
}