package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.mapper.ObjectMapper;
import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import io.github.gabrielwederson.help_desk_pro.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import io.github.gabrielwederson.help_desk_pro.mapper.ObjectMapper.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository repository;

    private TicketRequestDTO request;
    private Ticket ticket;


    @BeforeEach
    void setUp() {
        request = new TicketRequestDTO();
        request.setId(1L);
        request.setName("The printer broke");
        request.setDescription("Printer turns on but doesn't print.");
        request.setType(Type.PROBLEM);
        request.setPriority(Priority.MEDIUM);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setName("The printer broke");
        ticket.setDescription("Printer won't turn on anymore");
        ticket.setType(Type.PROBLEM);
        ticket.setPriority(Priority.HIGH);
    }

    @Test
    void createTicketSuccessfully() {

        when(repository.save(any(Ticket.class)))
                .thenReturn(ticket);

        TicketResponseDTO dto = ticketService.create(request);

        assertNotNull(dto);
        assertEquals(request.getName(), dto.getName());
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getType(), dto.getType());
        assertEquals(request.getPriority(), dto.getPriority());
        verify(repository).save(any(Ticket.class));

    }

    @Test
    void deleteTicketSuccessfully() {

        Long id = 2L;

        Ticket ticket2 = new Ticket();
        ticket2.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(ticket2));

        ticketService.delete(ticket2.getId());

        verify(repository).findById(id);
        verify(repository).delete(ticket2);
    }

    @Test
    void findByIdTicketSuccessfully() {
        Long id = 2L;

        Ticket ticket2 = new Ticket();
        ticket2.setId(id);
        ticket2.setName("System error");

        when(repository.findById(id))
                .thenReturn(Optional.of(ticket2));

        TicketResponseDTO dto = ticketService.findById(ticket2.getId());

        assertNotNull(dto);
        assertEquals(ticket2.getName(), dto.getName());

        verify(repository).findById(id);
    }

    @Test
    void updateTicketTicketSuccessfully() {

       when(repository.findById(1L))
               .thenReturn(Optional.of(ticket));

       ticket.setName(request.getName());
       ticket.setDescription(request.getDescription());
       ticket.setPriority(request.getPriority());
       ticket.setType(request.getType());

       when(repository.save(any(Ticket.class)))
               .thenReturn(ticket);

        TicketResponseDTO dto = ticketService.updateTicket(request);

        assertNotNull(dto);
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getPriority(), dto.getPriority());
        assertEquals(request.getPriority(), ticket.getPriority());

        verify(repository).findById(1L);
        verify(repository).save(ticket);
    }

    @Test
    void findAllTicketSuccessfully() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Ticket> page = new PageImpl<>(List.of(ticket));

        when(repository.findAll(pageable))
                .thenReturn(page);

        Page<TicketResponseDTO> response = ticketService.findAll(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        TicketResponseDTO dto = response.getContent().get(0);

        assertEquals(ticket.getName(), dto.getName());

        verify(repository).findAll(pageable);
    }

    @Test
    void findByPriorityTicketSuccessfully() {

        Priority priority = Priority.HIGH;

        Pageable pageable = PageRequest.of(0, 10);

        ticket.setPriority(Priority.HIGH);

        Page<Ticket> page = new PageImpl<>(List.of(ticket));

        when(repository.findByPriority(priority, pageable))
                .thenReturn(page);

        Page<TicketResponseDTO> response = ticketService.findByPriority(priority, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        TicketResponseDTO dto = response.getContent().get(0);

        assertEquals(ticket.getName(), dto.getName());
        assertEquals(ticket.getPriority(), dto.getPriority());

        verify(repository).findByPriority(priority, pageable);
    }

    @Test
    void findByTypeTicketSuccessfully() {

        Type type = Type.PROBLEM;

        Pageable pageable = PageRequest.of(0, 10);

        ticket.setType(Type.PROBLEM);

        Page<Ticket> page = new PageImpl<>(List.of(ticket));

        when(repository.findByType(type, pageable))
                .thenReturn(page);

        Page<TicketResponseDTO> response = ticketService.findByType(type, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        TicketResponseDTO dto = response.getContent().get(0);

        assertEquals(ticket.getName(), dto.getName());
        assertEquals(ticket.getType(), dto.getType());

        verify(repository).findByType(type, pageable);
    }

    @Test
    void findAllOrderByPriorityTicketSuccessfully() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Ticket> page = new PageImpl<>(List.of(ticket));

        when(repository.findAllOrderByPriority(pageable))
                .thenReturn(page);

        Page<TicketResponseDTO> response = ticketService.findAllOrderByPriority(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        TicketResponseDTO dto = response.getContent().get(0);

        assertEquals(ticket.getName(), dto.getName());

        verify(repository).findAllOrderByPriority(pageable);
    }

    @Test
    void markAsInProgressTicketSuccessfully() {

        Long id = 3L;

        Ticket ticketCreated = new Ticket();
        ticketCreated.setId(id);
        ticketCreated.setName("Pc Problem");
        ticketCreated.setDescription("PC won't turn on");
        ticketCreated.setStatus(Status.CREATED);
        ticketCreated.setPriority(Priority.HIGH);

        Ticket ticketInProgress = new Ticket();
        ticketInProgress.setId(id);
        ticketInProgress.setName("Pc Problem");
        ticketInProgress.setDescription("PC won't turn on");
        ticketInProgress.setStatus(Status.IN_PROGRESS);
        ticketInProgress.setPriority(Priority.HIGH);
        ticketInProgress.setCreatedAt(LocalDateTime.now());

        when(repository.findById(id))
                .thenReturn(Optional.of(ticketCreated));

        doNothing().when(repository).markAsInProgress(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(ticketCreated))
                .thenReturn(Optional.of(ticketInProgress));

        TicketResponseDTO dto = ticketService.markAsInProgress(id);

        assertNotNull(dto);
        assertEquals(Status.IN_PROGRESS, dto.getStatus());
        assertEquals(ticketCreated.getName(), dto.getName());
        assertEquals(ticketCreated.getDescription(), dto.getDescription());

        verify(repository, times(2)).findById(id);
        verify(repository).markAsInProgress(id);
    }

    @Test
    void markAsInCompleteTicketSuccessfully() {
        Long id = 3L;

        Ticket ticketInProgress = new Ticket();
        ticketInProgress.setId(id);
        ticketInProgress.setName("Pc Problem");
        ticketInProgress.setDescription("PC won't turn on");
        ticketInProgress.setStatus(Status.IN_PROGRESS);
        ticketInProgress.setPriority(Priority.HIGH);

        Ticket ticketComplete = new Ticket();
        ticketComplete.setId(id);
        ticketComplete.setName("Pc Problem");
        ticketComplete.setDescription("PC won't turn on");
        ticketComplete.setStatus(Status.COMPLETE);
        ticketComplete.setPriority(Priority.HIGH);
        ticketComplete.setCreatedAt(LocalDateTime.now());

        when(repository.findById(id))
                .thenReturn(Optional.of(ticketInProgress));

        doNothing().when(repository).markAsInComplete(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(ticketInProgress))
                .thenReturn(Optional.of(ticketComplete));

        TicketResponseDTO dto = ticketService.markAsInComplete(id);

        assertNotNull(dto);
        assertEquals(Status.COMPLETE, dto.getStatus());
        assertEquals(ticketInProgress.getName(), dto.getName());
        assertEquals(ticketInProgress.getDescription(), dto.getDescription());

        verify(repository, times(2)).findById(id);
        verify(repository).markAsInComplete(id);
    }
}