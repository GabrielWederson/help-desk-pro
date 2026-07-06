package io.github.gabrielwederson.help_desk_pro.repository;

import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT t FROM Ticket t WHERE t.priority = :priority")
    Page<Ticket> findByPriority(@Param("priority") Priority priority, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.type = :type")
    Page<Ticket> findByType(@Param("type") Type type, Pageable pageable);

    @Query("""
    SELECT t
    FROM Ticket t
    ORDER BY
        CASE t.priority
            WHEN io.github.gabrielwederson.help_desk_pro.model.enums.Priority.HIGH THEN 1
            WHEN io.github.gabrielwederson.help_desk_pro.model.enums.Priority.MEDIUM THEN 2
            WHEN io.github.gabrielwederson.help_desk_pro.model.enums.Priority.LOW THEN 3
        END
    """)
    Page<Ticket> findAllOrderByPriority(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.status = io.github.gabrielwederson.help_desk_pro.model.enums.Status.IN_PROGRESS WHERE t.id =:id")
    void markAsInProgress(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.status = io.github.gabrielwederson.help_desk_pro.model.enums.Status.COMPLETE WHERE t.id =:id")
    void markAsInComplete(@Param("id") Long id);
}
