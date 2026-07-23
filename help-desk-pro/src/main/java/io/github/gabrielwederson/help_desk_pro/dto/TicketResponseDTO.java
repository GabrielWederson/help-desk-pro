package io.github.gabrielwederson.help_desk_pro.dto;

import io.github.gabrielwederson.help_desk_pro.model.Ticket;
import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketResponseDTO implements Serializable {

    private static final Long serialVersionID = 1L;

    private Long id;

    private String name;

    private String description;

    private Type type;

    private Priority priority;

    private Status status;

    private String resolvedBy;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    public TicketResponseDTO() {
    }

    public TicketResponseDTO(Ticket ticket){
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.description = ticket.getDescription();
        this.type = ticket.getType();
        this.priority = ticket.getPriority();
        this.status = ticket.getStatus();
        this.resolvedBy = ticket.getResolvedBy();
        this.createdAt = ticket.getCreatedAt();
        this.completedAt = ticket.getCompletedAt();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketResponseDTO that = (TicketResponseDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && type == that.type && priority == that.priority && status == that.status && Objects.equals(resolvedBy, that.resolvedBy) && Objects.equals(createdAt, that.createdAt) && Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, priority, status, resolvedBy, createdAt, completedAt);
    }
}
