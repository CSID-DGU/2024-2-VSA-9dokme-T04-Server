package org.vsa.server.Notification.service;

import org.springframework.data.domain.PageImpl;
import org.vsa.server.Notification.dto.NotificationResponseDto;
import org.vsa.server.Notification.entity.Notification;
import org.vsa.server.Notification.repository.NotificationRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@NoArgsConstructor
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Page<NotificationResponseDto> findAllNotification(Long memberId, int pageNo){

        Pageable pageable = PageRequest.of(pageNo,10);

        Page<Notification> notifications = notificationRepository.findAllByMember_MemberId(memberId,pageable);


        List<NotificationResponseDto> myNotificationList = notifications.stream()
                .map(notification -> NotificationResponseDto.builder()
                        .notificationId(notification.getId())
                        .message(notification.getMessage())  // Null 체크
                        .startNotification(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .paramId(notification.getParamId())
                        .type(notification.getType())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(myNotificationList, pageable, notifications.getTotalElements());
    }


}
