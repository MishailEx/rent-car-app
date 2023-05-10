package com.example.autoannouncement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerRequestAnn {

    private Long category;

    private Long mark;

    private Long model;

    private Integer year;

    private Integer priceFrom;

    private Integer priceTo;
}
