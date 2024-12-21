package org.vsa.server.question.entity;

import org.vsa.server.book.entity.Book;
import org.vsa.server.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int questionId;

    private String title;

    private String content;

    private String nickName;

    private String email;

    private int chapter;

    private int bookPage;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Comment> timetableList = new ArrayList<>();

}
