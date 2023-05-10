package com.example.autoentity.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "auto")
public class Auto {
    @Id
    private Long id;

    private Long categoryId;

    private Long markId;

    private Long modelId;

    private Integer carYear;
}
