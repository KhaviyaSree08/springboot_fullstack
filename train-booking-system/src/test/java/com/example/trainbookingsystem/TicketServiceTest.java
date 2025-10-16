package com.example.trainbookingsystem;

import com.example.trainbookingsystem.entity.Ticket;
import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.entity.User;
import com.example.trainbookingsystem.repository.TicketRepository;
import com.example.trainbookingsystem.service.TicketService;
import com.example.trainbookingsystem.service.TrainService;
import com.example.trainbookingsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainService trainService;

    @InjectMocks
    private TicketService ticketService;

    private User user;
    private Train train;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Harini");

        train = new Train();
        train.setId(1L);
        train.setName("Express");
        train.setBasePrice(100.0);
        train.setDiscountPercentage(10.0);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setUser(user);
        ticket.setTrain(train);
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setFinalPrice(90.0);
    }

    @Test
    void getAllTickets_ShouldReturnListOfTickets() {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void getTicketById_WhenTicketExists_ShouldReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertTrue(result.isPresent());
        assertEquals(ticket.getId(), result.get().getId());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void getTicketById_WhenTicketDoesNotExist_ShouldReturnEmpty() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertFalse(result.isPresent());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void createTicket_ShouldSaveAndReturnTicket() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(trainService.getTrainById(1L)).thenReturn(Optional.of(train));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.createTicket(1L, 1L);

        assertNotNull(result);
        assertEquals(ticket.getFinalPrice(), result.getFinalPrice());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void updateTicket_WhenTicketExists_ShouldUpdateUserAndTrainAndReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket ticketDetails = new Ticket();
        ticketDetails.setTrain(train);
        ticketDetails.setUser(user);

        Ticket result = ticketService.updateTicket(1L, ticketDetails);

        assertNotNull(result);
        assertEquals(ticketDetails.getTrain(), result.getTrain());
        assertEquals(ticketDetails.getUser(), result.getUser());
        assertEquals(90.0, result.getFinalPrice());
        assertNotNull(result.getBookingDate()); 
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void updateTicket_WhenTicketDoesNotExist_ShouldThrowException() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> ticketService.updateTicket(1L, ticket));
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void deleteTicket_ShouldDeleteTicket() {
        doNothing().when(ticketRepository).deleteById(1L);

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void calculateTicketPrice_ShouldReturnCorrectPrice() {
        double price = ticketService.calculateTicketPrice(200.0, 25.0);
        assertEquals(150.0, price);
    }
}