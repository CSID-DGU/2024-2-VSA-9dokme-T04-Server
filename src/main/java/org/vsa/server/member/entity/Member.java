package org.vsa.server.member.entity;

import org.vsa.server.common.entity.BaseEntity;
import org.vsa.server.subscribe.entity.Subscribe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "subscribe_id")
    private Long subscribeId;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Subscribe subscribe;

    private String userRole;

    private String password;

    private String nickName;

    private String socialId;   //이메일

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    public enum SocialType {
        KAKAO
    }

    // 결제 검증용 키
    private String customerKey;
    private String authKey;

    private String billingKey;



    @Builder
    public Member(Long subscribeId, String userRole, String password, String nickName, String socialId, SocialType socialType, String customerKey, String authKey, String billingKey) {
        this.subscribeId = subscribeId;
        this.userRole = userRole;
        this.password = password;
        this.nickName = nickName;
        this.socialId = socialId;
        this.socialType = socialType;
        this.customerKey = customerKey;
        this.authKey = authKey;
        this.billingKey = billingKey;
    }
}
