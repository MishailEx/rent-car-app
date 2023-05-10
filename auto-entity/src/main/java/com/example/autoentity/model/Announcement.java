package com.example.autoentity.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    @Column("uuid")
    private String userUUID;
}
