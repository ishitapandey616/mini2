package com.nagarro.mini.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nagarro.mini.entity.User;
import com.nagarro.mini.service.UserService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public List<User> createUsers(@RequestParam int size) {
        return userService.createUsers(size);
    }

    @GetMapping
    public  Map<String, Object> getUsers(
            @RequestParam(defaultValue = "Name" ) String sortType,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset
    ) {
        return userService.getUsers(sortType, sortOrder, limit, offset);
    }
}

