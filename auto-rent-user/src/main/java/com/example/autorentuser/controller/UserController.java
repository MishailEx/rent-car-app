package com.example.autorentuser.controller;

import com.example.autoentity.model.User;
import com.example.autorentuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody User user) {
        User resp = userService.addUser(user);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody User user) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/delete/{uuid}")
    public ResponseEntity delete(@PathVariable String uuid) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/get/{uuid}")
    public ResponseEntity get(@PathVariable String uuid) {
        return ResponseEntity.ok(null);
    }
}
