package com.example.trainbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbookingsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}