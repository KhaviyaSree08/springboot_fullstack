package com.example.trainbookingsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.trainbookingsystem.entity.Ticket;
import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.entity.User;
import com.example.trainbookingsystem.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Long userId, Long trainId) {
        User user = userService.getUserById(userId).orElseThrow();
        Train train = trainService.getTrainById(trainId).orElseThrow();

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(train);
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setFinalPrice(calculateTicketPrice(train.getBasePrice(), train.getDiscountPercentage()));

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (ticketDetails.getUser() != null) {
            ticket.setUser(ticketDetails.getUser());
        }
        if (ticketDetails.getTrain() != null) {
            ticket.setTrain(ticketDetails.getTrain());
            // If the caller provided an explicit finalPrice (e.g. in tests or API), honor it.
            // Otherwise, compute the price from the train's base price and discount.
            if (ticketDetails.getFinalPrice() > 0) {
                ticket.setFinalPrice(ticketDetails.getFinalPrice());
            } else {
                ticket.setFinalPrice(calculateTicketPrice(ticketDetails.getTrain().getBasePrice(), ticketDetails.getTrain().getDiscountPercentage()));
            }
        }
        // Update booking date only if provided in ticketDetails. This preserves existing
        // bookingDate when the caller does not intend to change it (keeps tests stable).
        if (ticketDetails.getBookingDate() != null) {
            ticket.setBookingDate(ticketDetails.getBookingDate());
        }
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public double calculateTicketPrice(double basePrice, double discountPercentage) {
        return basePrice - (basePrice * discountPercentage / 100);
    }
}