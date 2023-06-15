package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.dto.UserNotification;
import com.example.registrationlogindemo.dto.wamchat.Response;
import com.example.registrationlogindemo.entity.MessageHistoryEntity;
import com.example.registrationlogindemo.repository.MessageHistoryRepository;
import com.example.registrationlogindemo.service.WammService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created By Kairat Zhiger
 * at 14.06.2023
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WammServiceImpl implements WammService {

    private String TOKEN = "5EDSaji2xar36iAd";
    private final MessageHistoryRepository messageHistoryRepository;
    private final WebClient webClient;

    @Override
    public Mono<Boolean> sendMessage(UserNotification userNotification) {
        return isWhatsappClient(getPhoneNumber(userNotification))
                .flatMap(isExist -> {
                    if (!isExist)
                        return Mono.just(Boolean.FALSE);

                    return webClient.get()
                            .uri(String.format("https://wamm.chat/api2/msg_to/%s/?phone=%s&text=%s", TOKEN, getPhoneNumber(userNotification), userNotification.getMessage()))
                            .exchangeToMono(
                                    response -> {
                                        if (response.statusCode().equals(HttpStatus.OK)) {
                                            return response.bodyToMono(String.class);
                                        } else {
                                            return response.createException().flatMap(Mono::error);
                                        }
                                    }
                            )

                            .flatMap(response -> {
                                log.info("Response: {}", response);
                                ObjectMapper objectMapper = new ObjectMapper();
                                Response clientResponse;
                                try {
                                    clientResponse = objectMapper.readValue(response, Response.class);

                                    if (clientResponse.getErr()==0)
                                        return Mono.just(Boolean.TRUE);


                                    return Mono.just(Boolean.FALSE);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                                return Mono.just(Boolean.FALSE);

                            })
                            .doOnSuccess(aBoolean -> log.info("Успешно отправлен  на номер {} сообщение {} ", getPhoneNumber(userNotification), userNotification.getMessage()))
                            .doOnError(aBoolean -> log.info("Ошибка при отправке сообщений"));
                });
    }

    private String getPhoneNumber(UserNotification userNotification) {
        if (userNotification.getClient() == null || userNotification.getClient().isBlank())
            return userNotification.getPhone();

        return userNotification.getClient();
    }

    @Override
    public List<Boolean> sendMessages(List<UserNotification> userNotifications) {
        return List.of(Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> isWhatsappClient(String phone) {
        log.info("Request: {}", String.format("https://wamm.chat/api2/check_phone/%s/?phone=%s", TOKEN, phone));
        return webClient
                .get()
                .uri((String.format("https://wamm.chat/api2/check_phone/%s/?phone=%s", TOKEN, phone)))
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToMono(String.class);
                            } else {
                                return response.createException().flatMap(Mono::error);
                            }
                        }
                )

                .flatMap(response -> {
                    log.info("Response: {}", response);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Response clientResponse;
                    try {
                        clientResponse = objectMapper.readValue(response, Response.class);

                        if (clientResponse.getErr()==0)
                            return Mono.just(Boolean.TRUE);


                        return Mono.just(Boolean.FALSE);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return Mono.just(Boolean.FALSE);

                })
                .doOnSuccess(aBoolean -> log.info("Успех запроса на проверку {}", phone))
                .doOnError(aBoolean -> log.info("Ошибка запроса на проверку {}", phone));
    }

    @Override
    public Page<MessageHistoryEntity> getMessageHistory() {
        return messageHistoryRepository.findAll(Pageable.ofSize(10));
    }

    @Override
    public Flux<MessageHistoryEntity> createMessageHistory(UserNotification userNotification) {
        if (userNotification.isFlag())
            return Flux.fromIterable(getMessageHistory().map(MessageHistoryEntity::getPhone))
                    .flatMap(s -> send(UserNotification.builder()
                            .phone(s)
                            .client(null)
                            .message(userNotification.getMessage())
                            .build())
                    );

        return Flux.just(send(userNotification)).flatMap(messageHistoryEntityMono -> messageHistoryEntityMono);

    }

    private Mono<MessageHistoryEntity> send(UserNotification userNotification) {
        return sendMessage(userNotification).
                map(message -> {
                    String status = "Доставлен";
                    if (!message) status = "Ошибка";
                    return messageHistoryRepository.save(MessageHistoryEntity.builder()
                            .createTime(LocalDateTime.now())
                            .message(userNotification.getMessage())
                            .phone(getPhoneNumber(userNotification))
                            .status(status)
                            .build());
                });
    }
}
