package com.example.autorentuser.service;

import com.example.autoentity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public User addUser(User user) {
       return mongoTemplate.save(user);
    }

}
