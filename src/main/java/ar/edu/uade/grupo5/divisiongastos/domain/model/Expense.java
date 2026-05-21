package ar.edu.uade.grupo5.divisiongastos.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public record Expense(
        ExpenseId id,
        GroupId groupId,
        MemberId payerId,
        BigDecimal amount,
        String description,
        LocalDate date,
        Set<MemberId> participants
) {

    public Expense {
        if (id == null || groupId == null || payerId == null) {
            throw new IllegalArgumentException("Expense ids are required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Expense description must not be blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("Expense date is required");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Expense participants are required");
        }
        amount = amount.setScale(2, java.math.RoundingMode.HALF_UP);
        description = description.trim();
        participants = Collections.unmodifiableSet(new LinkedHashSet<>(participants));
    }

    public static Expense create(
            GroupId groupId,
            MemberId payerId,
            BigDecimal amount,
            String description,
            Set<MemberId> participants
    ) {
        return new Expense(ExpenseId.newId(), groupId, payerId, amount, description, LocalDate.now(), participants);
    }
}
