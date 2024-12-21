package org.vsa.server.subscribe.repository;

import org.vsa.server.subscribe.entity.PaymentStatus;
import org.vsa.server.subscribe.entity.Subscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("SELECT s FROM Subscribe s WHERE s.expiredAt <= :date AND s.paymentStatus = :status")
    Page<Subscribe> findExpiredSubscriptions(@Param("date") LocalDate date, @Param("status") PaymentStatus status, Pageable pageable);

    Subscribe findByMember_MemberId(Long memberId);
}
