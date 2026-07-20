package io.github.gabrielwederson.help_desk_pro.dto;

import java.io.Serializable;
import java.util.Objects;

public class MarkTicketDTO implements Serializable {

    private static final Long serialVersionID = 1L;

    private Long id;

    private String email;

    public MarkTicketDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MarkTicketDTO that = (MarkTicketDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
