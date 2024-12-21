package org.vsa.server.subscribe.entity;

import org.vsa.server.common.entity.BaseEntity;
import org.vsa.server.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Subscribe{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long subscribeId;

    private LocalDate createdAt;

    private LocalDate expiredAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Boolean subscribeStatus;

    @OneToOne
    private Member  member;

}
