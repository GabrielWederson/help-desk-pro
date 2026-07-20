package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.MarkTicketDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.exceptions.TicketNotFoundException;
import io.github.gabrielwederson.help_desk_pro.exceptions.UserNotFoundException;
import io.github.gabrielwederson.help_desk_pro.exceptions.WrongStatusException;
import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.model.User;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import io.github.gabrielwederson.help_desk_pro.repository.TicketRepository;
import io.github.gabrielwederson.help_desk_pro.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.github.gabrielwederson.help_desk_pro.mapper.ObjectMapper.parseObjectMapper;
import static io.github.gabrielwederson.help_desk_pro.mapper.ObjectMapper.parseListObjectMapper;
import static org.bouncycastle.util.Strings.toLowerCase;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;

    @Autowired
    private UserRepository userRepository;

    private RabbitTemplate rabbitTemplate;

    public TicketService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public TicketResponseDTO create(TicketRequestDTO dto){

        var entity = parseObjectMapper(dto, Ticket.class);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setStatus(Status.CREATED);

        var response = parseObjectMapper(entity, TicketResponseDTO.class);

        repository.save(entity);

        return response;
    }

    @CacheEvict(value = "tickets", key = "#id")
    public void delete(Long id){
        Ticket entity = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with this id, not found") );

        repository.delete(entity);
    }

    @Cacheable(value = "tickets", key = "#id")
    public TicketResponseDTO findById(Long id){
        var entity = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with this id, not found"));

        return parseObjectMapper(entity, TicketResponseDTO.class);
    }

    public TicketResponseDTO updateTicket (TicketRequestDTO dto){
        Ticket entity = repository.findById(dto.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket with this id, not found"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPriority(dto.getPriority());
        entity.setType(dto.getType());

        repository.save(entity);

        return parseObjectMapper(entity, TicketResponseDTO.class);
    }

    public Page<TicketResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(TicketResponseDTO::new);
    }

    public Page<TicketResponseDTO> findByPriority(Priority priority, Pageable pageable) {
        return repository.findByPriority(priority, pageable)
                .map(TicketResponseDTO::new);
    }

    public Page<TicketResponseDTO> findByType (Type type, Pageable pageable){
        return repository.findByType(type, pageable)
                .map(TicketResponseDTO::new);
    }

    public Page<TicketResponseDTO> findAllOrderByPriority (Pageable pageable){
        return repository.findAllOrderByPriority(pageable)
                .map(TicketResponseDTO::new);
    }

    public Page<TicketResponseDTO> findAllTicketsComplete(Pageable pageable){
        return repository.findAllTicketsComplete(pageable)
                .map(TicketResponseDTO::new);
    }

    @CachePut(value = "tickets", key = "#id")
    @Transactional
    public TicketResponseDTO markAsInProgress(Long id){

        Ticket entity = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with this id, not found"));

        if (entity.getStatus() != Status.CREATED) {
            throw new WrongStatusException(
                    "Only tickets with CREATED status can be marked as IN_PROGRESS.");
        }

        entity.setStatus(Status.IN_PROGRESS);

        repository.save(entity);

        Ticket entity2 = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(""));

        return parseObjectMapper(entity2, TicketResponseDTO.class);
    }

    @CachePut(value = "tickets", key = "#request.getId()")
    @Transactional
    public TicketResponseDTO markAsInComplete(MarkTicketDTO request){

        Ticket entity = repository.findById(request.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket with this id, not found"));

        if (entity.getStatus() != Status.IN_PROGRESS) {
            throw new WrongStatusException(
                    "Only tickets with IN_PROGRESS status can be marked as COMPLETE.");
        }

        User user = userRepository.findNameByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("There are no users with that email"));

        entity.setPriority(Priority.COMPLETE);
        entity.setStatus(Status.COMPLETE);
        entity.setCompletedAt(LocalDateTime.now());
        entity.setResolvedBy(user.getName());
        entity.setUser(user);

        repository.save(entity);

        Ticket entity2 = repository.findById(request.getId())
                .orElseThrow(() -> new TicketNotFoundException(""));

        send(user.getEmail());

        return parseObjectMapper(entity2, TicketResponseDTO.class);

    }

    private void send(String email) {
        rabbitTemplate.convertAndSend(
                "ticket-complete.ex",
                "",
                email
        );
    }
}

