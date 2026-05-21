package ar.edu.uade.grupo5.divisiongastos.domain.model;

import java.util.UUID;

public record ExpenseId(String value) {

    public ExpenseId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Expense id must not be blank");
        }
    }

    public static ExpenseId newId() {
        return new ExpenseId(UUID.randomUUID().toString());
    }
}
