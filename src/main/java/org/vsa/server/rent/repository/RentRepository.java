package org.vsa.server.rent.repository;

import org.vsa.server.rent.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {

    Boolean existsByBookIdAndMemberId(Long bookId, Long memberId);
    Rent findByBookId(Long bookId);
    Rent findByBookIdAndMemberId(Long bookId, Long userId);
    Rent findByMemberId(Long memberId);


}
