package ar.edu.uade.grupo5.divisiongastos.domain.model;

import java.util.UUID;

public record GroupId(String value) {

    public GroupId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Group id must not be blank");
        }
    }

    public static GroupId newId() {
        return new GroupId(UUID.randomUUID().toString());
    }
}
