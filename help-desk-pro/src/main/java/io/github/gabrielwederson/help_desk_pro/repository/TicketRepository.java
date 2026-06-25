package io.github.gabrielwederson.help_desk_pro.repository;

import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
