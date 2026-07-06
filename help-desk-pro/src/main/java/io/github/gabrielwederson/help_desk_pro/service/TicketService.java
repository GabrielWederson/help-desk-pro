package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import io.github.gabrielwederson.help_desk_pro.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.github.gabrielwederson.help_desk_pro.mapper.ObjectMapper.parseObjectMapper;
import static io.github.gabrielwederson.help_desk_pro.mapper.ObjectMapper.parseListObjectMapper;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;


    public TicketResponseDTO create(TicketRequestDTO dto){

        var entity = parseObjectMapper(dto, Ticket.class);
        entity.setCreatedAt(LocalDateTime.now());

        var response = parseObjectMapper(entity, TicketResponseDTO.class);

        repository.save(entity);

        return response;
    }

    public void delete(Long id){
        Ticket entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException() ); //change this exception after

        repository.delete(entity);
    }

    public TicketResponseDTO findById(Long id){
        var entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException()); //change this exception after

        return parseObjectMapper(entity, TicketResponseDTO.class);

    }

    public TicketResponseDTO updateTicket (TicketRequestDTO dto){
        Ticket entity = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException()); //change this exception after

        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setPriority(dto.getPriority());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());

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

    @Transactional
    public TicketResponseDTO markAsInProgress(Long id){

        Ticket entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        if (entity.getStatus() != Status.CREATED) {
            throw new IllegalStateException(
                    "Only tickets with CREATED status can be marked as IN_PROGRESS."); //change this exception after
        }

        repository.markAsInProgress(id);

        entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        return parseObjectMapper(entity, TicketResponseDTO.class);
    }

    @Transactional
    public TicketResponseDTO markAsInComplete(Long id){

        Ticket entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        if (entity.getStatus() != Status.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only tickets with CREATED status can be marked as IN_PROGRESS."); //change this exception after
        }

        repository.markAsInComplete(id);

        entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        return parseObjectMapper(entity, TicketResponseDTO.class);
    }
}

