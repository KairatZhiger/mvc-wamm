package com.example.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created By Kairat Zhiger
 * at 15.06.2023
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="wamm_message_history")
public class MessageHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String message;
    private String status;
    private LocalDateTime createTime;
}
