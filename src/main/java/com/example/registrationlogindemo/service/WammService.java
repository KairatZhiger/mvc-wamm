package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.dto.UserNotification;
import com.example.registrationlogindemo.entity.MessageHistoryEntity;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created By Kairat Zhiger
 * at 14.06.2023
 */
public interface WammService {

    Mono<Boolean> sendMessage(UserNotification userNotification);
    List<Boolean> sendMessages(List<UserNotification> userNotifications);
    Mono<Boolean> isWhatsappClient(String phone);
    Page<MessageHistoryEntity> getMessageHistory();
    Flux<MessageHistoryEntity> createMessageHistory(UserNotification userNotification);

}
