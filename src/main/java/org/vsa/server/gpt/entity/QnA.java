package org.vsa.server.gpt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.vsa.server.common.entity.BaseEntity;

@Getter
@Setter
@Entity
public class QnA extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long qnaId;

    private String bookName;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;
}
