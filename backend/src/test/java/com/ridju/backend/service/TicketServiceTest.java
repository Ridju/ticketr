package com.ridju.backend.service;

import com.ridju.backend.domain.dto.TicketDTO;
import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Role;
import com.ridju.backend.domain.model.Ticket;
import com.ridju.backend.domain.util.ERole;
import com.ridju.backend.repository.MyUserRepository;
import com.ridju.backend.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private MyUserRepository userRepository;
    private TicketService ticketService;

    private String username;
    private String password;
    private String email;
    private List<Role> roles;
    private String ticketTitle;
    private String ticketDescription;
    private boolean done;


    @BeforeEach
    void initTestcase() {
        this.ticketRepository = Mockito.mock(TicketRepository.class);
        this.userRepository = Mockito.mock(MyUserRepository.class);
        this.ticketService = new TicketService(ticketRepository, userRepository);

        this.username = "testuser";
        this.password =  "testpassword";
        this.email = "testuser@test.com";
        this.roles = Arrays.asList(new Role(ERole.ADMIN.label));
        this.ticketTitle = "tickettitle";
        this.ticketDescription = "super ticket text";
        this.done = false;
    }

    @Test
    void givenUserAndTicket_whenCreateTicket_thenShouldPass() {
        //given
        MyUser myUser = new MyUser();
        myUser.setUsername(this.username);
        myUser.setEmail(this.email);
        myUser.setPassword(this.password);
        myUser.setRoles(this.roles);

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle(this.ticketTitle);
        ticketDTO.setDescription(this.ticketDescription);
        ticketDTO.setDone(this.done);

        //when
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).then(returnsFirstArg());
        Ticket returnTicket = this.ticketService.createTicket(ticketDTO);

        //then
        assertThat(returnTicket).isNotNull();
        assertEquals(returnTicket.getTitle(), this.ticketTitle);
        assertEquals(returnTicket.getDescription(), this.ticketDescription);
        assertEquals(returnTicket.isDone(), this.done);
    }

    @Test
    void givenUserAndTicket_whenGetTicket_thenShouldPass() {
        //given
        MyUser myUser = new MyUser();
        myUser.setUsername(this.username);
        myUser.setEmail(this.email);
        myUser.setPassword(this.password);
        myUser.setRoles(this.roles);

        Ticket ticket = new Ticket(this.ticketTitle, this.ticketDescription, myUser, this.done);
        long id = 1;
        //when
        Mockito.when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        Ticket returnTicket = ticketService.getTicket(id);
        //then
        assertEquals(returnTicket.getTitle(), ticket.getTitle());
        assertEquals(returnTicket.getDate(), ticket.getDate());
        assertEquals(returnTicket.getDescription(), ticket.getDescription());
        assertEquals(returnTicket.isDone(), ticket.isDone());
    }

    @Test
    void givenListOfTickets_whenGetAllTickets_thenShouldPass() {
        //given
        Page<Ticket> alltickets = new PageImpl<>(Arrays.asList(
                new Ticket(this.ticketTitle, this.ticketDescription, this.done),
                new Ticket(this.ticketTitle, this.ticketDescription, this.done),
                new Ticket(this.ticketTitle, this.ticketDescription, this.done),
                new Ticket(this.ticketTitle, this.ticketDescription, this.done)
        ));

        //when
        Mockito.when(ticketRepository.findAll(Mockito.any(PageRequest.class))).thenReturn((Page<Ticket>) alltickets);
        List<Ticket> returnTickets = this.ticketService.getAllTickets();
        //then
        assertEquals(returnTickets.size(), alltickets.getSize());
    }

    @Test
    void givenTwoUsers_whenFindTicketByAssignedTo_thenShouldReturnFilteredTicketList() {
        //given
        MyUser myUserOne = new MyUser();
        myUserOne.setUsername(this.username + "1");
        myUserOne.setEmail(this.email);
        myUserOne.setPassword(this.password);
        myUserOne.setRoles(this.roles);

        MyUser myUserTwo = new MyUser();
        myUserTwo.setUsername(this.username + "2");
        myUserTwo.setEmail(this.email);
        myUserTwo.setPassword(this.password);
        myUserTwo.setRoles(this.roles);

        List<Ticket> alltickets = Arrays.asList(
                new Ticket(this.ticketTitle, this.ticketDescription, myUserOne,  this.done),
                new Ticket(this.ticketTitle, this.ticketDescription, myUserOne, this.done),
                new Ticket(this.ticketTitle, this.ticketDescription, myUserTwo, this.done),
                new Ticket(this.ticketTitle, this.ticketDescription, myUserOne, this.done)
        );
        long id = 1;
        //when
        Mockito.when(ticketRepository.findTicketByAssignedTo(myUserOne))
                .thenReturn(alltickets.stream()
                        .filter(t -> t.getAssignedTo().equals(myUserOne))
                        .collect(Collectors.toList()));
        List<Ticket> returnTickets = this.ticketRepository.findTicketByAssignedTo(myUserOne);
        //then

        assertEquals(returnTickets.stream().count(), 3);
    }

    @Test
    void givenUpdatedTicket_whenUpdateTicket_thenShouldReturnUpdatedTicket() {
        //Given
        long id = 1;
        MyUser myUser = new MyUser();
        myUser.setUsername(this.username);
        myUser.setEmail(this.email);
        myUser.setPassword(this.password);
        myUser.setRoles(this.roles);

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(id);
        ticketDTO.setTitle(this.ticketTitle);
        ticketDTO.setDescription(this.ticketDescription);
        ticketDTO.setDone(this.done);

        String updatedTitle = "Updated title";

        //when
        Mockito.when(ticketRepository.findById(id)).thenReturn(Optional.of(new Ticket(ticketDTO)));
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).then(returnsFirstArg());
        ticketDTO.setTitle(updatedTitle);
        Ticket updatedTicket = ticketService.updateTicket(ticketDTO);

        //then
        assertEquals(updatedTicket.getTitle(), updatedTitle);
    }

    @Test
    void givenTicketAndUser_whenAssignToTuser_thenShouldAssignTicketToUser() {
        //given
        MyUser myUser = new MyUser();
        myUser.setUsername(this.username);
        myUser.setEmail(this.email);
        myUser.setPassword(this.password);
        myUser.setRoles(this.roles);

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle(this.ticketTitle);
        ticketDTO.setDescription(this.ticketDescription);
        ticketDTO.setDone(this.done);
        long id = 1;
        //when
        Mockito.when(ticketRepository.findById(id)).thenReturn(Optional.of(new Ticket(ticketDTO)));
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(myUser));
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).then(returnsFirstArg());
        Ticket updateTicket = ticketService.assignToUser(id, id);
        //then
        assertEquals(updateTicket.getAssignedTo().getUsername(), myUser.getUsername());
    }

    @Test
    void givenTicket_whenMarkTicketAs_thenShouldTicketUpdate() {
        //given
        Ticket ticket = new Ticket(this.ticketTitle, this.ticketDescription, this.done);
        long id = 1;
        boolean done = true;
        //then
        Mockito.when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).then(returnsFirstArg());
        Ticket updatedTicket = ticketService.markTicketAs(id, done);

        //when
        assertEquals(updatedTicket.isDone(), done);
    }
}
