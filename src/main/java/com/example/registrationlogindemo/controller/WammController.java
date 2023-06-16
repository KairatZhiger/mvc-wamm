package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.dto.UserNotification;
import com.example.registrationlogindemo.entity.MessageHistoryEntity;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.service.WammService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created By Kairat Zhiger
 * at 14.06.2023
 */
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class WammController {

    private final WammService wammService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMessages(Model model) {
        var users = wammService.getMessageHistory();
        var clients=users.map(MessageHistoryEntity::getPhone);
        model.addAttribute("clients", clients);
        model.addAttribute("users", users);
        model.addAttribute("userInfo", new UserNotification());
        return "users";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Mono<String> sendMessage(Model model, @ModelAttribute UserNotification userInfo) {
        System.out.println(userInfo.toString());
        return wammService.createMessageHistory(userInfo)
                .collectList().map(
                        messageHistoryEntities -> "redirect:/users/"
                );
    }
}
