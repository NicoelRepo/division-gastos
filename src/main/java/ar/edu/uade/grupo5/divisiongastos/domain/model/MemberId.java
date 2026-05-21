package ar.edu.uade.grupo5.divisiongastos.domain.model;

import java.util.UUID;

public record MemberId(String value) {

    public MemberId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Member id must not be blank");
        }
    }

    public static MemberId newId() {
        return new MemberId(UUID.randomUUID().toString());
    }
}
