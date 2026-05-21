package ar.edu.uade.grupo5.divisiongastos.application.service;

import ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence.InMemoryExpenseRepository;
import ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence.InMemoryGroupRepository;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementServiceTest {

    private final GroupService groupService = new GroupService(new InMemoryGroupRepository());
    private final ExpenseService expenseService = new ExpenseService(new InMemoryExpenseRepository(), groupService);
    private final SettlementService settlementService = new SettlementService(groupService, expenseService);

    @Test
    void computesTransfersFromStoredExpenses() {
        var group = groupService.createGroup("Viaje");
        var ana = groupService.addMember(group.id(), "Ana");
        var beto = groupService.addMember(group.id(), "Beto");
        expenseService.addExpense(
                group.id(),
                ana.id(),
                new BigDecimal("100.00"),
                "Cena",
                Set.of(ana.id(), beto.id())
        );

        var transfers = settlementService.computeSettlement(group.id());

        assertThat(transfers).singleElement().satisfies(transfer -> {
            assertThat(transfer.from()).isEqualTo(beto.id());
            assertThat(transfer.to()).isEqualTo(ana.id());
            assertThat(transfer.amount()).isEqualByComparingTo("50.00");
        });
    }
}
