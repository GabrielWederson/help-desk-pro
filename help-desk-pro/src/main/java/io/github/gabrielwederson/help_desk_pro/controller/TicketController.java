package io.github.gabrielwederson.help_desk_pro.controller;

import io.github.gabrielwederson.help_desk_pro.controller.docs.TicketControllerDocs;
import io.github.gabrielwederson.help_desk_pro.dto.MarkTicketDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.TicketResponseDTO;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import io.github.gabrielwederson.help_desk_pro.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ticket/v1")
@Tag(name = "Tickets", description = "endpoints to management tickets")
public class TicketController implements TicketControllerDocs{

    @Autowired
    private TicketService service;

    @GetMapping
    @Override
    public Page<TicketResponseDTO> findAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @PostMapping
    @Override
    public TicketResponseDTO create(@RequestBody TicketRequestDTO dto){
        return service.create(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    @Override
    public TicketResponseDTO findById(@PathVariable("id")Long id){
        return service.findById(id);
    }

    @PutMapping
    @Override
    public TicketResponseDTO updateTicket(@RequestBody TicketRequestDTO dto){
        return service.updateTicket(dto);
    }

    @GetMapping(value = "/priority/{priority}")
    @Override
    public Page<TicketResponseDTO> findByPriority(@PathVariable("priority") Priority priority, Pageable pageable){
        return service.findByPriority(priority, pageable);
    }

    @GetMapping(value = "/type/{type}")
    @Override
    public Page<TicketResponseDTO> findByType(@PathVariable("type") Type type, Pageable pageable){
        return service.findByType(type, pageable);
    }

    @GetMapping("/orderByAllPrioryties")
    @Override
    public Page<TicketResponseDTO> findAllOrderByPriority(Pageable pageable){
        return service.findAllOrderByPriority(pageable);
    }

    @GetMapping("/complete")
    @Override
    public Page<TicketResponseDTO> findAllTicketsComplete(Pageable pageable){
        return service.findAllTicketsComplete(pageable);
    }

    @PatchMapping(value = "/{id}")
    @Override
    public TicketResponseDTO markAsInProgress(@PathVariable("id") Long id){
        return service.markAsInProgress(id);
    }

    @PatchMapping(value = "/complete")
    @Override
    public TicketResponseDTO markAsInComplete(@RequestBody MarkTicketDTO request){
        return service.markAsInComplete(request);
    }
}
