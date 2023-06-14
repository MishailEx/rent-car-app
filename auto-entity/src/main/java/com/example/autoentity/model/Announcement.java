package com.example.autoentity.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "announcement")
public class Announcement {

    @Id
    private Long id;

    private String name;

    @Column("auto_id")
    private Long autoId;

    private Integer price;

    private String description;

    @Column("user_uuid")
    private String userUUID;

    @Column("chat_id")
    private List<Integer> chat = new ArrayList<>();

    @Column("image_id")
    private List<String> images = new ArrayList<>();

    public Announcement(Long autoId, Integer price, String description, String userUUID) {
        this.autoId = autoId;
        this.price = price;
        this.description = description;
        this.userUUID = userUUID;
    }
}
