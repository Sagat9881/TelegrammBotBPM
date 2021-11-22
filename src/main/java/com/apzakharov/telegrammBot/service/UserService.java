package com.apzakharov.telegrammBot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public Optional<User> findByChatId(Long Chat_id) {
        return userRepository.findByChatId(Chat_id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public List<User> findNewUsers() {
        List<User> users = userRepository.findNewUsers();

        users.forEach((user) -> user.setNotified(true));
        userRepository.saveAll(users);

        return users;
    }

    @Transactional
    public User addUser(User user) {
        user.setAdmin(userRepository.count() == 0);
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }



}

