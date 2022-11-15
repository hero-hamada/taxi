package com.epam.taxi.entity;

import lombok.Data;

@Data
public class Vehicle {
    private String id;
    private Double x;
    private Double y;
    private Double distance;
}
