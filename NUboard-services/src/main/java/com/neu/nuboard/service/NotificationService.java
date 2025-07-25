package com.neu.nuboard.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    public void sendNotification(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String formattedMessage = String.format("[%s] %s", timestamp, message);
        System.out.println("This is a notification test: " + formattedMessage);
    }

    /**
     * 发送一条用户管理相关的通知到控制台日志。
     * 用于记录用户的创建、更新、删除等操作。
     */
    // 用户管理通知
    public void sendUserManagementNotification(
            String id,
            String username,
            String email,
            String program,
            String locationId,
            String collegeId,
            String locationName,
            String collegeName,
            int eventsCount,
            String timestamp
    ) {
        String message = String.format(
            "User Management Notification: id=%s, username=%s, email=%s, program=%s, locationId=%s, collegeId=%s, locationName=%s, collegeName=%s, eventsCount=%d, timestamp=%s",
            id, username, email, program, locationId, collegeId, locationName, collegeName, eventsCount, timestamp
        );
        sendNotification(message);
    }

    /**
     * 发送一条活动注册相关的通知到控制台日志。
     * 用于记录用户报名、取消报名等与活动注册相关的操作。
     */
    // 活动注册通知
    public void sendEventRegistrationNotification(
            String registrationId,
            String userId,
            String username,
            String email,
            String program,
            String locationName,
            String collegeName,
            String eventId,
            String eventTitle,
            String eventDescription,
            String eventAddress,
            String eventCreatorId,
            String eventOrganizerType,
            String eventLocationName,
            String eventLocationId,
            String eventStartTime,
            String eventEndTime
    ) {
        String message = String.format(
            "Event Registration Notification: registrationId=%s, userId=%s, username=%s, email=%s, program=%s, locationName=%s, collegeName=%s, eventId=%s, eventTitle=%s, eventDescription=%s, eventAddress=%s, eventCreatorId=%s, eventOrganizerType=%s, eventLocationName=%s, eventLocationId=%s, eventStartTime=%s, eventEndTime=%s",
            registrationId, userId, username, email, program, locationName, collegeName, eventId, eventTitle, eventDescription, eventAddress, eventCreatorId, eventOrganizerType, eventLocationName, eventLocationId, eventStartTime, eventEndTime
        );
        sendNotification(message);
    }

    /**
     * 发送一条活动管理相关的通知到控制台日志。
     * 用于记录活动的创建、更新、删除等管理操作。
     */
    // 活动管理通知
    public void sendEventManagementNotification(
            String id,
            String title,
            String description,
            String startTime,
            String endTime,
            String locationId,
            String address,
            String creatorId,
            String organizerType,
            int registrationsCount
    ) {
        String message = String.format(
            "Event Management Notification: id=%s, title=%s, description=%s, startTime=%s, endTime=%s, locationId=%s, address=%s, creatorId=%s, organizerType=%s, registrationsCount=%d",
            id, title, description, startTime, endTime, locationId, address, creatorId, organizerType, registrationsCount
        );
        sendNotification(message);
    }
}
