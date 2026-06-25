package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;

}
