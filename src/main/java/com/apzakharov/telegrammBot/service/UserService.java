package com.apzakharov.telegrammBot.service;

import com.apzakharov.telegrammBot.bot.ChatBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.repo.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public User findByChatId(long id) {
        return userRepository.findByChatId(id);
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

