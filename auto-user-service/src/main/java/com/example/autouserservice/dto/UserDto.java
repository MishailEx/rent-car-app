package com.example.autouserservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String uuid;

    private String role;

    private String name;

    private String email;

    private String phone;


}
