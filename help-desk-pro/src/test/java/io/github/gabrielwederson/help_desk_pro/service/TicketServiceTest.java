package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.MarkTicketDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.exceptions.*;
import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.model.User;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import io.github.gabrielwederson.help_desk_pro.repository.TicketRepository;
import io.github.gabrielwederson.help_desk_pro.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private UserRepository userRepository;

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
    void deleteTicketFailedByIdNotFound(){
        Long id = 11L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.delete(id);
        });

        verify(repository, never()).delete(any(Ticket.class));
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
    void findByIdFailedByIdNotFound(){
        Long id = 122L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.findById(id);
        });
    }

    @Test
    void updateTicketSuccessfully() {

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
    void updateTicketFailedByIdNotFound(){
        TicketRequestDTO request2 = new TicketRequestDTO();
        request.setId(1L);

        when(repository.findById(request2.getId()))
                .thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> ticketService.updateTicket(request2));

        verify(repository, never()).save(any(Ticket.class));
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

        when(repository.save(any(Ticket.class)))
                .thenReturn(ticketCreated);

        when(repository.findById(id))
                .thenReturn(Optional.of(ticketCreated))
                .thenReturn(Optional.of(ticketInProgress));

        TicketResponseDTO dto = ticketService.markAsInProgress(id);

        assertNotNull(dto);
        assertEquals(Status.IN_PROGRESS, dto.getStatus());
        assertEquals(ticketCreated.getName(), dto.getName());
        assertEquals(ticketCreated.getDescription(), dto.getDescription());

        verify(repository, times(2)).findById(id);
        verify(repository).save(ticketCreated);
    }

    @Test
    void markAsInProgressFailedByIdNotFound(){
        Long id = 999L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.findById(id);
        });

        verify(repository, never()).markAsInProgress(id);
    }

    @Test
    void markAsInProgressFailedByStatusIsNotCreated(){
        Long id = 1333L;

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(Status.IN_PROGRESS);

        when(repository.findById(id)).thenReturn(Optional.of(ticket));

        WrongStatusException exception = assertThrows(
                WrongStatusException.class,
                () -> ticketService.markAsInProgress(id)
        );

        assertEquals(
                "Only tickets with CREATED status can be marked as IN_PROGRESS.",
                exception.getMessage()
        );

        verify(repository, never()).markAsInProgress(anyLong());
    }

    @Test
    void markAsInCompleteTicketSuccessfully() {

        Long id = 3L;

        MarkTicketDTO request = new MarkTicketDTO();
        request.setId(id);
        request.setEmail("gabriel@email.com");

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
        ticketComplete.setPriority(Priority.COMPLETE);
        ticketComplete.setCreatedAt(LocalDateTime.now());

        User user = new User();
        user.setId(1L);
        user.setName("Gabriel");

        when(repository.findById(id))
                .thenReturn(Optional.of(ticketInProgress))
                .thenReturn(Optional.of(ticketComplete));

        when(userRepository.findNameByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(repository.save(any(Ticket.class)))
                .thenReturn(ticketInProgress);

        TicketResponseDTO dto = ticketService.markAsInComplete(request);

        assertNotNull(dto);
        assertEquals(Status.COMPLETE, dto.getStatus());
        assertEquals(ticketInProgress.getName(), dto.getName());
        assertEquals(ticketInProgress.getDescription(), dto.getDescription());

        verify(repository, times(2)).findById(id);
        verify(userRepository).findNameByEmail(request.getEmail());
        verify(repository).save(ticketInProgress);
    }

    @Test
    void markAsInCompleteFailedByIdNotFound() {

        Long id = 997L;

        MarkTicketDTO request = new MarkTicketDTO();
        request.setId(id);
        request.setEmail("gabriel@email.com");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.markAsInComplete(request)
        );

        verify(repository).findById(id);
        verify(userRepository, never()).findNameByEmail(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void markAsInCompleteFailedByStatusIsNotInProgress() {

        Long id = 1222L;

        MarkTicketDTO request = new MarkTicketDTO();
        request.setId(id);
        request.setEmail("gabriel@email.com");

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(Status.CREATED);

        when(repository.findById(id)).thenReturn(Optional.of(ticket));

        WrongStatusException exception = assertThrows(
                WrongStatusException.class,
                () -> ticketService.markAsInComplete(request)
        );

        assertEquals(
                "Only tickets with IN_PROGRESS status can be marked as COMPLETE.",
                exception.getMessage()
        );

        verify(repository).findById(id);
        verify(userRepository, never()).findNameByEmail(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void findAllTicketsComplete(){
        Pageable pageable = PageRequest.of(0, 10);

        ticket.setStatus(Status.COMPLETE);

        Page<Ticket> page = new PageImpl<>(List.of(ticket));

        when(repository.findAllTicketsComplete(pageable))
                .thenReturn(page);

        Page<TicketResponseDTO> response = ticketService.findAllTicketsComplete(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        TicketResponseDTO dto = response.getContent().get(0);

        assertEquals(ticket.getName(), dto.getName());
        assertEquals(Status.COMPLETE, dto.getStatus());

        verify(repository).findAllTicketsComplete(pageable);
    }

}