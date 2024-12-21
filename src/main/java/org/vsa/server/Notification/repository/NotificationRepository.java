package org.vsa.server.Notification.repository;

import org.vsa.server.Notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {

    Page<Notification> findAllByMember_MemberId(Long memberId, Pageable pageable);
}
