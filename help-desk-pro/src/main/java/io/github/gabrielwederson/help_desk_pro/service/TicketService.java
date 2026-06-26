package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
