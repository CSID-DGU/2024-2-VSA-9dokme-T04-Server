package org.vsa.server.book.entity;

import org.vsa.server.common.entity.BaseEntity;
import org.vsa.server.member.entity.Member;
import org.vsa.server.question.entity.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    private String title;

    private LocalDateTime publishDate;

    private String author;

    private String publisher;

    private String category;

    private String description;

    private String bookImage;   //이미지 URL

    private String bookURL; // URL로 프론트에 넘겨주기로 했기 때문에 데이터타입 수정

    private int bookChapter;

    private int bookFullPage;

    private Integer rent;

    //[merge] member 어떻게 join?
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Question> timetableList = new ArrayList<>();


    public static Book create(String title, LocalDateTime publishDate, String author, String publisher,
                              String category, String description, String bookImage, String bookURL,
                              int bookChapter, int bookFullPage, Integer rent) {
        return Book.builder()
                .title(title)
                .publishDate(publishDate)
                .author(author)
                .publisher(publisher)
                .category(category)
                .description(description)
                .bookImage(bookImage)
                .bookURL(bookURL)
                .bookChapter(bookChapter)
                .bookFullPage(bookFullPage)
                .rent(rent)
                .build();
    }

    public void update(String title, LocalDateTime publishDate, String author, String publisher,
                       String category, String description, String bookImage, String bookURL,
                       int bookChapter, int bookFullPage, Integer rent) {
        this.title = title;
        this.publishDate = publishDate;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.description = description;
        this.bookImage = bookImage;
        this.bookURL = bookURL;
        this.bookChapter = bookChapter;
        this.bookFullPage = bookFullPage;
        this.rent = rent;
    }
}

