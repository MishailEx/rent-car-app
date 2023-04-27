package com.example.autoentity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auto {

    private Long id;

    private Category category;

    private Mark mark;

    private Model model;

    private Integer year;
}
