package com.example.trainbookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String source;
    private String destination;
    private double basePrice;
    private double discountPercentage;  // e.g., 10 for 10% discount

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}