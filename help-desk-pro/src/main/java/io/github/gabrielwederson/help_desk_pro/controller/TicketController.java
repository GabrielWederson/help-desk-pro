package io.github.gabrielwederson.help_desk_pro.controller;

import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import io.github.gabrielwederson.help_desk_pro.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ticket/v1")
public class TicketController {

    @Autowired
    private TicketService service;

    @PostMapping
    public TicketResponseDTO create(TicketRequestDTO dto){
        return service.create(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public TicketResponseDTO findById(@PathVariable("id")Long id){
        return service.findById(id);
    }

    @PutMapping
    public TicketResponseDTO updateTicket(@RequestBody TicketRequestDTO dto){
        return service.updateTicket(dto);
    }

    @GetMapping
    public Page<TicketResponseDTO> findAll(Pageable pageable){
      return service.findAll(pageable);
    }

    @GetMapping(value = "/{priority}")
    public Page<TicketResponseDTO> findByPriority(Priority priority, Pageable pageable){
        return service.findByPriority(priority, pageable);
    }

    @GetMapping(value = "/{type}")
    public Page<TicketResponseDTO> findByType(Type type, Pageable pageable){
        return service.findByType(type, pageable);
    }

    @GetMapping("/orderByAllPrioryties")
    public Page<TicketResponseDTO> findAllOrderByPriority(Pageable pageable){
        return service.findAllOrderByPriority(pageable);
    }

    @PatchMapping(value = "/{id}")
    public TicketResponseDTO markAsInProgress(@PathVariable("id") Long id){
        return service.markAsInProgress(id);
    }

    @PatchMapping(value = "/complete/{id}")
    public TicketResponseDTO markAsInComplete(@PathVariable("id") Long id){
        return service.markAsInComplete(id);
    }
}
