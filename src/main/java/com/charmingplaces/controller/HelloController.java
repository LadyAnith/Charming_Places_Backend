package com.example.apirestfirebase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apirestfirebase.dto.MessageDto;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public ResponseEntity<MessageDto> getHello() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(new MessageDto("Hello " + username));
	}

}
