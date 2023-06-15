package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.MessageHistoryEntity;
import com.example.registrationlogindemo.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By Kairat Zhiger
 * at 15.06.2023
 */
public interface MessageHistoryRepository extends JpaRepository<MessageHistoryEntity, Long> {
    @Override
    Page<MessageHistoryEntity> findAll(Pageable pageable);
}
