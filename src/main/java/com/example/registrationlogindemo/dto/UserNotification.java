package com.example.registrationlogindemo.dto;

import com.example.registrationlogindemo.enums.Role;
import lombok.*;

/**
 * Created By Kairat Zhiger
 * at 14.06.2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserNotification {
    private String phone;
    private String message;
    private String client;
    private boolean flag;

}
