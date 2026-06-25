package io.github.gabrielwederson.help_desk_pro.dto;

import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;

import java.time.LocalDateTime;
import java.util.Objects;

public class TicketResponseDTO {

    private String name;

    private String description;

    private Type type;

    private Priority priority;

    private Status status;

    private Long userID;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    public TicketResponseDTO() {
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

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketResponseDTO that = (TicketResponseDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && type == that.type && priority == that.priority && status == that.status && Objects.equals(userID, that.userID) && Objects.equals(createdAt, that.createdAt) && Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, type, priority, status, userID, createdAt, completedAt);
    }
}
