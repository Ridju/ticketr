package com.ridju.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridju.backend.domain.dto.TicketDTO;
import com.ridju.backend.domain.model.Ticket;
import com.ridju.backend.service.MyUserService;
import com.ridju.backend.service.TicketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerTest {

    private String title = "testTitle";
    private String description = "testdescription";
    private long id = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    public void givenId_whenGetTicket_thenReturnTicket() throws Exception {
        //given
        Ticket ticket = new Ticket();
        ticket.setId(this.id);
        ticket.setTitle(this.title);
        ticket.setDescription(this.description);

        //when
        Mockito.when(ticketService.getTicket(this.id))
                .thenReturn(ticket);

        //then
        mockMvc.perform(get("/ticket/" + this.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toIntExact(this.id))))
                .andExpect(jsonPath("$.title", is(this.title)));
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    public void givenListOfTickets_whenGetAllTickets_thenReturnAllTickets() throws Exception {
        //given
        Ticket ticket = new Ticket();
        ticket.setId(this.id);
        ticket.setTitle(this.title);
        ticket.setDescription(this.description);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);
        ticketList.add(ticket);

        //when
        Mockito.when(ticketService.getAllTickets())
                .thenReturn(ticketList);

        //then
        mockMvc.perform(get("/tickets/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER"})
    public void givenTicketDTO_whenCreateTicket_thenReturnNewTicket() throws Exception {
        //given
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle(this.title);
        ticketDTO.setDescription(this.description);

        Ticket ticket = new Ticket();
        ticket.setId(this.id);
        ticket.setTitle(this.title);
        ticket.setDescription(this.description);

        //when
        Mockito.when(ticketService.createTicket(ticketDTO))
                .thenReturn(ticket);

        //then
        mockMvc.perform(post("/ticket/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    public void givenUser_whenGetTicketByUser_thenReturnTicketList() throws Exception {
        //given
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle(this.title);
        ticketDTO.setDescription(this.description);

        Ticket ticket = new Ticket();
        ticket.setId(this.id);
        ticket.setTitle(this.title);
        ticket.setDescription(this.description);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);

        //when
        Mockito.when(ticketService.getTicketsByUser(this.id))
                .thenReturn(ticketList);

        //then
        mockMvc.perform(get("/ticketsByUserId/" + this.id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    public void givenTicket_whenUpdateTicket_thenReturnUpdatedTicket() throws Exception {
        //given
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle(this.title);
        ticketDTO.setDescription(this.description);

        Ticket ticket = new Ticket();
        ticket.setId(this.id);
        ticket.setTitle(this.title);
        ticket.setDescription(this.description);

        //when
        Mockito.when(ticketService.updateTicket(ticketDTO))
                .thenReturn(ticket);

        //then
        mockMvc.perform(put("/ticket/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    public void givenTicket_whenDeleteTicket_thenReturnSuccess() throws Exception {
        //given

        //when
        Mockito.doNothing().when(ticketService).deleteTicket(this.id);

        //then
        mockMvc.perform(delete("/ticket/" + this.id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    public void givenTicket_whenMarkTicketAsDone_thenReturnSuccess() throws Exception {
        //given
        Ticket ticket = new Ticket();
        ticket.setId(this.id);
        ticket.setTitle(this.title);
        ticket.setDescription(this.description);
        Boolean done = true;

        //when
        Mockito.when(ticketService.markTicketAs(this.id, true))
                .thenReturn(ticket);

        //then
        mockMvc.perform(put("/ticket/" + this.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(done)))
                .andExpect(status().isOk());
    }
}
