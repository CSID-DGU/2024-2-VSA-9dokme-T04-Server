package org.vsa.server.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private Long id;
    private String email;
    private String kakaoName;
}
