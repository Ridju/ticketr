package com.ridju.backend.service;

import com.ridju.backend.domain.dto.TicketDTO;
import com.ridju.backend.domain.exceptions.TicketNotFoundException;
import com.ridju.backend.domain.exceptions.UserNotFoundException;
import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Ticket;
import com.ridju.backend.repository.MyUserRepository;
import com.ridju.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final MyUserRepository userRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, MyUserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    //create ticket
    public Ticket createTicket(TicketDTO ticketDTO) {
        Ticket ticket = ticketDTO.mapToEntity();
        return this.ticketRepository.save(ticket);
    }

    //read ticket
    public Ticket getTicket(long id) {
        Optional<Ticket> ticketOptional = this.ticketRepository.findById(id);
        if (ticketOptional.isEmpty()) {
            throw new TicketNotFoundException("Ticket with Id={" + id + "} could not be found");
        }
        return ticketOptional.get();
    }

    //read all ticktes
    public List<Ticket> getAllTickets() {
        return this.ticketRepository.findAll(PageRequest.of(0, 10)).getContent();
    }

    //read ticket by user
    public List<Ticket> getTicketsByUser(long id) {
        MyUser user = this.userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User with id={" + id + "} could not be found"));
        return this.ticketRepository.findTicketByAssignedTo(user);
    }

    //update ticket
    public Ticket updateTicket(TicketDTO ticketDTO) {
        Ticket ticket = this.ticketRepository.findById(ticketDTO.getId()).orElseThrow(() ->
                new TicketNotFoundException("Ticket with Id={" + ticketDTO.getId() + "} could not be found"));
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setDone(ticketDTO.getDone());
        ticket.setAssignedTo(ticketDTO.getAssignedTo());
        return this.ticketRepository.save(ticket);
    }

    //delete ticket
    public void deleteTicket(long id) {
        this.ticketRepository.deleteById(id);
    }

    //assign ticket to user with id
    public Ticket assignToUser(long ticketId, long userId) {
        MyUser user = this.userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id={" + userId + "} could not be found"));
        Ticket ticket = this.ticketRepository.findById(ticketId).orElseThrow(() ->
                new TicketNotFoundException("Ticket with id={" + ticketId + "} could not be found"));

        ticket.setAssignedTo(user);
        return this.ticketRepository.save(ticket);
    }

    //mark ticket as done
    public Ticket markTicketAs(long ticketId, boolean done) {
        Ticket ticket = this.ticketRepository.findById(ticketId).orElseThrow(() ->
                new TicketNotFoundException("Ticket with id={" + ticketId + "} could not be found"));

        ticket.setDone(done);
        return this.ticketRepository.save(ticket);
    }
}
