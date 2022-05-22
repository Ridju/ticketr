package com.ridju.backend.repository;

import com.ridju.backend.domain.model.MyUser;
import com.ridju.backend.domain.model.Ticket;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
    Optional<Ticket> findById(Long aLong);

    List<Ticket> findTicketByAssignedTo(MyUser user);
}
