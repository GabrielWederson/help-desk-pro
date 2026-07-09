package io.github.gabrielwederson.help_desk_pro.dto;

import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;

import java.time.LocalDateTime;
import java.util.Objects;

public class TicketRequestDTO {

    private Long id;

    private String name;

    private String description;

    private Type type;

    private Priority priority;

    public TicketRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequestDTO that = (TicketRequestDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && type == that.type && priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, priority);
    }
}
