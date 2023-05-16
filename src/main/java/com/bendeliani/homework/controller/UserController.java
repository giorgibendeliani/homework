package com.bendeliani.homework.controller;

import com.bendeliani.homework.dto.UserDto;
import com.bendeliani.homework.model.User;
import com.bendeliani.homework.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody UserDto userDto) {
        userService.signUpUser(convert(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private User convert(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
