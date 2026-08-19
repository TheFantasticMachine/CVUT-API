package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.User;
import com.testgen.restapi.api.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    public List<User> getUsers() {
        return repo.findAll();
    }

    public User getUserById(int id) {
        return repo.findById(id).orElse(new User());
    }

    public User addUser(User user) {
        return repo.save(user);
    }


}
