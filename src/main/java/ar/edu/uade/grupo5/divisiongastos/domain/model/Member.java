package ar.edu.uade.grupo5.divisiongastos.domain.model;

public record Member(MemberId id, String name) {

    public Member {
        if (id == null) {
            throw new IllegalArgumentException("Member id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name must not be blank");
        }
        name = name.trim();
    }
}
