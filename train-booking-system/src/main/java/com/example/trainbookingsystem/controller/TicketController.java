package com.example.trainbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.trainbookingsystem.entity.Ticket;
import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.entity.User;
import com.example.trainbookingsystem.service.TicketService;
import com.example.trainbookingsystem.service.TrainService;
import com.example.trainbookingsystem.service.UserService;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;

    @GetMapping
    public String getAllTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "tickets/list";
    }

    @GetMapping("/new")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("trains", trainService.getAllTrains());
        return "tickets/form";
    }

    @PostMapping
    public String createTicket(@RequestParam Long userId, @RequestParam Long trainId) {
        ticketService.createTicket(userId, trainId);
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String editTicketForm(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.getTicketById(id).orElseThrow());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("trains", trainService.getAllTrains());
        return "tickets/form";
    }

    @PostMapping("/update/{id}")
    public String updateTicket(@PathVariable Long id, @RequestParam Long userId, @RequestParam Long trainId) {
        Ticket ticket = ticketService.getTicketById(id).orElseThrow();
        User user = userService.getUserById(userId).orElseThrow();
        Train train = trainService.getTrainById(trainId).orElseThrow();
        ticket.setUser(user);
        ticket.setTrain(train);
        ticketService.updateTicket(id, ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "redirect:/tickets";
    }
}