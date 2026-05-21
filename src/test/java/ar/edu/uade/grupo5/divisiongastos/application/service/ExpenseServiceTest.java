package ar.edu.uade.grupo5.divisiongastos.application.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.DomainException;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;
import ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence.InMemoryExpenseRepository;
import ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence.InMemoryGroupRepository;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpenseServiceTest {

    private final InMemoryGroupRepository groupRepository = new InMemoryGroupRepository();
    private final GroupService groupService = new GroupService(groupRepository);
    private final ExpenseService expenseService = new ExpenseService(new InMemoryExpenseRepository(), groupService);

    @Test
    void addsExpenseForValidGroupMembers() {
        var group = groupService.createGroup("Casa");
        var ana = groupService.addMember(group.id(), "Ana");
        var beto = groupService.addMember(group.id(), "Beto");

        var expense = expenseService.addExpense(
                group.id(),
                ana.id(),
                new BigDecimal("100.00"),
                "Supermercado",
                Set.of(ana.id(), beto.id())
        );

        assertThat(expenseService.listExpenses(group.id())).containsExactly(expense);
    }

    @Test
    void rejectsPayerOutsideGroup() {
        var group = groupService.createGroup("Casa");
        var ana = groupService.addMember(group.id(), "Ana");

        assertThatThrownBy(() -> expenseService.addExpense(
                group.id(),
                new MemberId("outside"),
                new BigDecimal("100.00"),
                "Supermercado",
                Set.of(ana.id())
        )).isInstanceOf(DomainException.class)
                .hasMessageContaining("pagador");
    }

    @Test
    void rejectsEmptyParticipants() {
        var group = groupService.createGroup("Casa");
        var ana = groupService.addMember(group.id(), "Ana");

        assertThatThrownBy(() -> expenseService.addExpense(
                group.id(),
                ana.id(),
                new BigDecimal("100.00"),
                "Supermercado",
                Set.of()
        )).isInstanceOf(DomainException.class)
                .hasMessageContaining("participante");
    }
}
