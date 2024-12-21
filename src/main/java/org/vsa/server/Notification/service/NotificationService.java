package org.vsa.server.Notification.service;

import org.vsa.server.Notification.entity.Notification;
import org.vsa.server.Notification.repository.NotificationRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Page<Notification> findAllNotification(Long memberId, int pageNo){

        Pageable pageable = PageRequest.of(pageNo,8);

        Page<Notification> notifications = notificationRepository.findAllByMember_MemberId(memberId,pageable);

        return notifications;
    }


}
