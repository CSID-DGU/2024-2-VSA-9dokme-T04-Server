package org.vsa.server.payment.entity;

import org.vsa.server.common.entity.BaseEntity;
import org.vsa.server.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String pgProvider;
    private String payMethod;
    private String merchantUid;
    private String name;
    private int amount;
    private String buyerEmail;
    private String buyerName;
    private String buyerTel;
    private String buyerAddr;
    private String buyerPostcode;
    private String impUid;

    private int retryCount;

    private String status;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}

