package com.ridju.backend.domain.model;

import com.ridju.backend.domain.dto.TicketDTO;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;

    @OneToOne
    @JoinColumn(name = "id")
    private MyUser assignedTo;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date date;

    private boolean done;

    public Ticket() {
    }

    public Ticket(String title, String description, boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public Ticket(TicketDTO ticketDTO) {
        this.title = ticketDTO.getTitle();
        this.description = ticketDTO.getDescription();
        this.done = ticketDTO.getDone();
//        this.assignedTo = ticketDTO.getAssignedTo();
    }

    public Ticket(String title, String description, MyUser assignedTo, Boolean done) {
        this.title = title;
        this.description = description;
        this.assignedTo = assignedTo;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
