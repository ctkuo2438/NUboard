package com.neu.nuboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.neu.nuboard.dto.EventRegistrationDTO;
import com.neu.nuboard.dto.EventResponseDTO;
import com.neu.nuboard.dto.UserCreateDTO;
import com.neu.nuboard.model.Event;
import com.neu.nuboard.model.EventRegistration;
import com.neu.nuboard.model.User;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final String USER_MANAGEMENT_TOPIC = "nuboard.user.management";
    private static final String EVENT_MANAGEMENT_TOPIC = "nuboard.event.management";
    private static final String EVENT_REGISTRATION_TOPIC = "nuboard.event.registration";

    private static final String USER_CREATED = "user_created";
    private static final String USER_UPDATED = "user_updated";
    private static final String USER_DELETED = "user_deleted";

    private static final String EVENT_CREATED = "event_created";
    private static final String EVENT_UPDATED = "event_updated";
    private static final String EVENT_DELETED = "event_deleted";
    
    private static final String EVENT_REGISTRATION = "event_registered";
    private static final String EVENT_UNREGISTERED = "event_unregistered";

    
    /*
     * Send all user information to Kafka
     */
    public void sendUserEvent(String type, User user) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setId(user.getId() != null ? user.getId().toString() : null);
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProgram(user.getProgram());
        dto.setLocationId(user.getLocation() != null ? user.getLocation().getId() : null);
        dto.setCollegeId(user.getCollege() != null ? user.getCollege().getId() : null);
        dto.setLocationName(user.getLocation() != null ? user.getLocation().getName() : null);
        dto.setCollegeName(user.getCollege() != null ? user.getCollege().getName() : null);
        dto.setEventsCount(user.getRegistrations() != null ? user.getRegistrations().size() : 0);
        kafkaTemplate.send(USER_MANAGEMENT_TOPIC, dto)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Message sent successfully: " + type + " - " + user.getUsername());
                    System.out.println("   Topic: " + result.getRecordMetadata().topic());
                    System.out.println("   Partition: " + result.getRecordMetadata().partition());
                    System.out.println("   Offset: " + result.getRecordMetadata().offset());
                } else {
                    System.err.println("Message sending failed: " + ex.getMessage());
                }
            });
    }
   public void sendUserCreatedEvent(User user){
    sendUserEvent(USER_CREATED, user);
   }
   public void sendUserUpdatedEvent(User user){
    sendUserEvent(USER_UPDATED, user);
   }
   public void sendUserDeletedEvent(User user){
    sendUserEvent(USER_DELETED, user); 
   }

   public void sendEventEvent(String type, Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setLocationId(event.getLocationId());
        dto.setAddress(event.getAddress());
        dto.setCreatorId(event.getCreatorId());
        dto.setOrganizerType(event.getOrganizerType()); 
        kafkaTemplate.send(EVENT_MANAGEMENT_TOPIC, dto);
    }
   public void sendEventCreatedEvent(Event event){
    sendEventEvent(EVENT_CREATED, event);
   }
   public void sendEventUpdatedEvent(Event event){
    sendEventEvent(EVENT_UPDATED, event);
   }
   public void sendEventDeletedEvent(Event event){
    sendEventEvent(EVENT_DELETED, event);
}

    /**
     * 1. 将EventRegistration、User、Event中的所有关键信息（如username、email、eventTitle等）赋值到EventRegistrationDTO。
     * 2. 需要在EventRegistrationDTO类中补全字段，否则Producer赋值和Consumer取值时会报错或拿不到数据。
     * 3. 保证Producer、DTO、Consumer、NotificationService字段一一对应
     */
   public void sendEventRegistrationEvent(String type, EventRegistration eventRegistration) {
        EventRegistrationDTO dto = new EventRegistrationDTO();
        dto.setId(eventRegistration.getId());
        dto.setEventId(eventRegistration.getEvent() != null ? eventRegistration.getEvent().getId() : null);
        dto.setUserId(eventRegistration.getUser() != null ? eventRegistration.getUser().getId() : null);
        // 用户相关
        if (eventRegistration.getUser() != null) {
            dto.setUsername(eventRegistration.getUser().getUsername());
            dto.setEmail(eventRegistration.getUser().getEmail());
            dto.setProgram(eventRegistration.getUser().getProgram());
            dto.setLocationName(eventRegistration.getUser().getLocation() != null ? eventRegistration.getUser().getLocation().getName() : null);
            dto.setCollegeName(eventRegistration.getUser().getCollege() != null ? eventRegistration.getUser().getCollege().getName() : null);
        }
        // 活动相关
        if (eventRegistration.getEvent() != null) {
            dto.setEventTitle(eventRegistration.getEvent().getTitle());
            dto.setEventDescription(eventRegistration.getEvent().getDescription());
            dto.setEventAddress(eventRegistration.getEvent().getAddress());
            dto.setEventCreatorId(eventRegistration.getEvent().getCreatorId());
            dto.setEventOrganizerType(eventRegistration.getEvent().getOrganizerType() != null ? eventRegistration.getEvent().getOrganizerType().name() : null);
            dto.setEventLocationName(eventRegistration.getEvent().getLocation() != null ? eventRegistration.getEvent().getLocation().getName() : null);
            dto.setEventLocationId(eventRegistration.getEvent().getLocation() != null ? String.valueOf(eventRegistration.getEvent().getLocation().getId()) : null);
            dto.setEventStartTime(eventRegistration.getEvent().getStartTime() != null ? eventRegistration.getEvent().getStartTime().toString() : null);
            dto.setEventEndTime(eventRegistration.getEvent().getEndTime() != null ? eventRegistration.getEvent().getEndTime().toString() : null);
        }
        kafkaTemplate.send(EVENT_REGISTRATION_TOPIC, dto);
    }

   public void sendEventRegisteredEvent(EventRegistration eventRegistration){
    sendEventRegistrationEvent(EVENT_REGISTRATION, eventRegistration);
   }

   public void sendEventUnregisteredEvent(EventRegistration eventRegistration){
    sendEventRegistrationEvent(EVENT_UNREGISTERED, eventRegistration);
   }

}