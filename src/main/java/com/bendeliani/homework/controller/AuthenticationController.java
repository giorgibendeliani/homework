package com.bendeliani.homework.controller;

import com.bendeliani.homework.config.security.JwtTokenUtil;
import com.bendeliani.homework.dto.AuthRequestDto;
import com.bendeliani.homework.dto.AuthResponseDto;
import com.bendeliani.homework.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RequestMapping("api/v1")
@RestController
public class AuthenticationController {

	private final AuthenticationManager authManager;
	private final JwtTokenUtil jwtUtil;
	
	@PostMapping("/auth/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto request) {
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(), request.getPassword())
			);
			
			User user = (User) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			AuthResponseDto response = new AuthResponseDto(user.getEmail(), accessToken);
			
			return ResponseEntity.ok().body(response);
			
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
