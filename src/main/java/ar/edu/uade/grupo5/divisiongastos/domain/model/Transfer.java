package ar.edu.uade.grupo5.divisiongastos.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Transfer(MemberId from, MemberId to, BigDecimal amount) {

    public Transfer {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Transfer members are required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
