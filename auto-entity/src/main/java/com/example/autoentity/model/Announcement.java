package com.example.autoentity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {

    private Long id;

    private Auto auto;

    private Integer price;

    private String description;

    private String userUUID;


}
