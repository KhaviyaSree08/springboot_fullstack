package com.example.trainbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbookingsystem.entity.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
}