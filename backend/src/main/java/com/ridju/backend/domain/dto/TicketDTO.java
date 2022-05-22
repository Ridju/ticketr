package com.ridju.backend.domain.dto;

import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Ticket;

import java.time.LocalDateTime;

public class TicketDTO {
    private long id;
    private String title;
    private String description;
    private MyUser assignedTo;
    private LocalDateTime date;
    private boolean done;

    public TicketDTO() {
    }

    public Ticket mapToEntity() {
        Ticket ticket = new Ticket();
        ticket.setTitle(this.title);
        ticket.setDone(this.done);
        ticket.setAssignedTo(this.assignedTo);
        ticket.setDescription(this.description);
        ticket.setId(this.id);
        return ticket;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyUser getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(MyUser assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", assignedTo=" + assignedTo +
                ", date=" + date +
                ", done=" + done +
                '}';
    }
}
