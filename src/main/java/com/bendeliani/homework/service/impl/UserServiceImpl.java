package com.bendeliani.homework.service.impl;

import com.bendeliani.homework.model.User;
import com.bendeliani.homework.repository.UserRepository;
import com.bendeliani.homework.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public User signUpUser(User user) {
    final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
    user.setPassword(encryptedPassword);
    return userRepository.save(user);
  }
}
