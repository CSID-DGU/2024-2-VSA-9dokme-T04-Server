package org.vsa.server.book.repository;

import org.vsa.server.book.entity.Book;
import org.vsa.server.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategory(String category);
    Page<Book> findByTitleContaining(String Title,Pageable pageable);

    Page<Book> findAll(Pageable pageable);

    List<Book> findAll();

    Page<Book> findAllByCategory(String category, Pageable pageable);

    Boolean existsByTitle(String Title);

    Boolean existsByAuthor(String author);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByTitleContaining(String Title);

//    Book findByBookId(int bookId);

    Page<Book> findAllByMember(Member member, Pageable pageable);
    Book findByBookId(Long bookId);

    //readAt 기준 정렬
//    @Query("SELECT b FROM Book b JOIN Rent r ON b.bookId = r.bookId WHERE r.memberId = :memberId ORDER BY r.readAt DESC")
//    Page<Book> findBooksByMemberOrderByReadAtDesc(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN Bookmark bm ON b.bookId = bm.book.bookId WHERE bm.member.memberId = :memberId")
    Page<Book> findBooksByMember(@Param("memberId") Long memberId, Pageable pageable);


}
