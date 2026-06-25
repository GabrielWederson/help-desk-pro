package io.github.gabrielwederson.help_desk_pro.model;

import io.github.gabrielwederson.help_desk_pro.model.enums.Priority;
import io.github.gabrielwederson.help_desk_pro.model.enums.Status;
import io.github.gabrielwederson.help_desk_pro.model.enums.Type;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    private static final Long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 130)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "user_id")
    private Long userId;

    public Ticket() {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(name, ticket.name) && Objects.equals(description, ticket.description) && type == ticket.type && priority == ticket.priority && status == ticket.status && Objects.equals(userId, ticket.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, priority, status, userId);
    }
}
