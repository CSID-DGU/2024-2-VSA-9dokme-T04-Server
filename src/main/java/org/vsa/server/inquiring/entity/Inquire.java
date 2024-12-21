package org.vsa.server.inquiring.entity;

import org.vsa.server.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inquire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquire_id")
    private Long inquireId;

    @Column(name = "user_id")
    private Long userId;

    private String title;

    private String content;

    private Boolean status;

}
