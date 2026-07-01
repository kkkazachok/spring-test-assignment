package com.guide.city_guide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDTO {
    private Long id;
    private String name;
    private String category;
    private Double latitude;
    private Double longitude;
    private String description;
    private Double averageRating;
    private Double distance; // расстояние до пользователя
}