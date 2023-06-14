package com.example.autoentity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String uuid;

    private String role;

    private String username;

    private String password;

    private String email;

    private String phone;

    public User(String role, String username, String password) {
        this.role = role;
        this.username = username;
        this.password = password;
    }
}
