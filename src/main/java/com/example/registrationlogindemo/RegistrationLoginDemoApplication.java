package com.example.registrationlogindemo;

import com.example.registrationlogindemo.entity.Role;
import com.example.registrationlogindemo.repository.RoleRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@SpringBootApplication
@RequiredArgsConstructor
public class RegistrationLoginDemoApplication {

//	private final RoleRepository roleRepository;
//	private final UserRepository userRepository;
//	@PostConstruct
//	void init(){
//		roleRepository.save(new Role(3L,"ADMIN", Collections.singletonList(userRepository.findByEmail("87714888328@mail.ru"))));
//	}
	public static void main(String[] args) {
		SpringApplication.run(RegistrationLoginDemoApplication.class, args);
	}

}
