package com.neu.nuboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.neu.nuboard.dto.EventRegistrationDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.dto.UserCreateDTO;

@Service
public class KafkaConsumerService {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "nuboard.user.management", groupId = "nuboard-group")
    public void consumeUserManagementEvent(UserCreateDTO msg) {
        notificationService.sendUserManagementNotification(
            msg.getId(),
            msg.getUsername(),
            msg.getEmail(),
            msg.getProgram(),
            msg.getLocationId() != null ? msg.getLocationId().toString() : null,
            msg.getCollegeId() != null ? msg.getCollegeId().toString() : null,
            msg.getLocationName(),
            msg.getCollegeName(),
            msg.getEventsCount(),
            "" // 占位，原timestamp参数，现传空字符串
        );
    }

    /**
     * 1. 需要在EventResponseDTO中添加type和timestamp字段
     * 2. Producer端组装EventResponseDTO对象时，填充type和timestamp字段。
     * 3. Consumer端直接接收EventResponseDTO对象，取字段传递给NotificationService。
     * 4. 其它业务逻辑不变。
     * 
     * 当前本类event部分未做DTO切换。
     */
    @KafkaListener(topics = "nuboard.event.management", groupId = "nuboard-group")
    public void consumeEventManagementEvent(EventResponseDTO msg) {
        notificationService.sendEventManagementNotification(
            msg.getId() != null ? String.valueOf(msg.getId()) : null,
            msg.getTitle(),
            msg.getDescription(),
            msg.getStartTime() != null ? msg.getStartTime().toString() : null,
            msg.getEndTime() != null ? msg.getEndTime().toString() : null,
            msg.getLocationId() != null ? msg.getLocationId().toString() : null,
            msg.getAddress(),
            msg.getCreatorId() != null ? String.valueOf(msg.getCreatorId()) : null,
            msg.getOrganizerType() != null ? msg.getOrganizerType().toString() : null,
            0 // 占位，原registrationsCount参数，现传0
        );
    }

    @KafkaListener(topics = "nuboard.event.registration", groupId = "nuboard-group")
    public void consumeEventRegistrationEvent(EventRegistrationDTO msg) {
        notificationService.sendEventRegistrationNotification(
            msg.getId() != null ? String.valueOf(msg.getId()) : null,
            msg.getUserId() != null ? String.valueOf(msg.getUserId()) : null,
            msg.getUsername(),
            msg.getEmail(),
            msg.getProgram(),
            msg.getLocationName(),
            msg.getCollegeName(),
            msg.getEventId() != null ? String.valueOf(msg.getEventId()) : null,
            msg.getEventTitle(),
            msg.getEventDescription(),
            msg.getEventAddress(),
            msg.getEventCreatorId() != null ? String.valueOf(msg.getEventCreatorId()) : null,
            msg.getEventOrganizerType(),
            msg.getEventLocationName(),
            msg.getEventLocationId(),
            msg.getEventStartTime(),
            msg.getEventEndTime()
        );
    }
}
