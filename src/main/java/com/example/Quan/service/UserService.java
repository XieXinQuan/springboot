package com.example.Quan.service;

import com.example.Quan.entity.User;
import com.example.Quan.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserRepository userRepository;

    public Boolean add(String name, String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);

        return true;
    }
    public Boolean update(String name, String password){
        User allByNameLike = userRepository.findAllById(1L);
        if(allByNameLike != null){
            password = password == null? ""+(int)(Math.random()*10000) : password;
            allByNameLike.setPassword(password);
            userRepository.save(allByNameLike);
        }

        return true;
    }
}
