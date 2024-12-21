package org.vsa.server.member.repository;

import org.vsa.server.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findBySocialId(String email);
    Member findByMemberId(Long id);
    Member findByCustomerKey(String customerKey);
    List<Member> findAll();
    Page<Member> findAll(Pageable pageable);

    Boolean existsBySocialId(String socialId);
}
