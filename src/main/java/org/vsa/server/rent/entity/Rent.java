package org.vsa.server.rent.entity;

import org.vsa.server.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

//pdf 웹 뷰 조회시 갱신되는 엔티티
@Entity
@Data
@NoArgsConstructor
public class Rent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Long rentId;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime rentDate;

    private LocalDateTime returnDate;  //반납 처리는 db에서 삭제하는게 좋을 것 같아서 필요없는 필드값인거 같습니다.

    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime readAt;

    private Float progress;

    private int lastPage;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "subscribe_id")
    private Long subscribeId;

    @Column(name = "book_id")
    private Long bookId;
}
