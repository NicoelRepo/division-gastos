package ar.edu.uade.grupo5.divisiongastos.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExpenseView(
        String description,
        BigDecimal amount,
        LocalDate date,
        String payerName,
        List<String> participantNames
) {
}
