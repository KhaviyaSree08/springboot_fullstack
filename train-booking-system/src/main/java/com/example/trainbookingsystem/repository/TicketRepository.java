package com.example.trainbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trainbookingsystem.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}