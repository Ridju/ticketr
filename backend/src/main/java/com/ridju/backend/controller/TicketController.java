package com.ridju.backend.controller;

import com.ridju.backend.domain.dto.TicketDTO;
import com.ridju.backend.domain.model.Ticket;
import com.ridju.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Secured("ROLE_STAFF")
    @GetMapping("/ticket/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable long id) {
        return ResponseEntity.ok(this.ticketService.getTicket(id));
    }

    @Secured("ROLE_STAFF")
    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTicktes() {
        return ResponseEntity.ok(this.ticketService.getAllTickets());
    }

    @Secured("ROLE_USER")
    @PostMapping("/ticket")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketDTO ticketDTO){
        return ResponseEntity.ok(this.ticketService.createTicket(ticketDTO));
    }

    @Secured("ROLE_STAFF")
    @GetMapping("/ticketsByUserId/{id}")
    public ResponseEntity<List<Ticket>> getTicketByUser(@PathVariable long id) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(id));
    }

    @Secured("ROLE_STAFF")
    @PutMapping("/ticket")
    public ResponseEntity<Ticket> updateTicket(@RequestBody TicketDTO ticketDTO) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketDTO));
    }

    @Secured("ROLE_STAFF")
    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable long id) {
        this.ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }


    @Secured("ROLE_STAFF")
    @PutMapping("/ticket/{id}")
    public ResponseEntity<Ticket> markTicketAsDone(@PathVariable long id, @RequestBody boolean done) {
        return ResponseEntity.ok(this.ticketService.markTicketAs(id, done));
    }
}
