package ar.edu.uade.grupo5.divisiongastos.domain.service;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Expense;
import ar.edu.uade.grupo5.divisiongastos.domain.model.ExpenseId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Member;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BalanceCalculatorTest {

    private final BalanceCalculator calculator = new BalanceCalculator();
    private final GroupId groupId = new GroupId("group");
    private final MemberId anaId = new MemberId("ana");
    private final MemberId betoId = new MemberId("beto");
    private final MemberId caroId = new MemberId("caro");
    private final Group group = new Group(groupId, "Viaje", List.of(
            new Member(anaId, "Ana"),
            new Member(betoId, "Beto"),
            new Member(caroId, "Caro")
    ));

    @Test
    void calculatesSimpleSharedExpense() {
        var expense = expense("food", anaId, "100.00", linkedSet(anaId, betoId));

        var balance = calculator.calculate(group, List.of(expense));

        assertThat(balance.get(anaId)).isEqualByComparingTo("50.00");
        assertThat(balance.get(betoId)).isEqualByComparingTo("-50.00");
        assertThat(balance.get(caroId)).isEqualByComparingTo("0.00");
    }

    @Test
    void supportsPayerOutsideParticipants() {
        var expense = expense("kids", anaId, "90.00", linkedSet(betoId, caroId));

        var balance = calculator.calculate(group, List.of(expense));

        assertThat(balance.get(anaId)).isEqualByComparingTo("90.00");
        assertThat(balance.get(betoId)).isEqualByComparingTo("-45.00");
        assertThat(balance.get(caroId)).isEqualByComparingTo("-45.00");
    }

    @Test
    void assignsRoundingResidueToLastParticipant() {
        var expense = expense("taxi", anaId, "100.00", linkedSet(anaId, betoId, caroId));

        var balance = calculator.calculate(group, List.of(expense));

        assertThat(balance.get(anaId)).isEqualByComparingTo("66.67");
        assertThat(balance.get(betoId)).isEqualByComparingTo("-33.33");
        assertThat(balance.get(caroId)).isEqualByComparingTo("-33.34");
    }

    @Test
    void handlesCrossedExpenses() {
        var first = expense("dinner", anaId, "120.00", linkedSet(anaId, betoId, caroId));
        var second = expense("tickets", betoId, "60.00", linkedSet(anaId, betoId));

        var balance = calculator.calculate(group, List.of(first, second));

        assertThat(balance.get(anaId)).isEqualByComparingTo("50.00");
        assertThat(balance.get(betoId)).isEqualByComparingTo("-10.00");
        assertThat(balance.get(caroId)).isEqualByComparingTo("-40.00");
    }

    private Expense expense(String description, MemberId payerId, String amount, Set<MemberId> participants) {
        return new Expense(
                new ExpenseId(description),
                groupId,
                payerId,
                new BigDecimal(amount),
                description,
                LocalDate.of(2026, 5, 21),
                participants
        );
    }

    private LinkedHashSet<MemberId> linkedSet(MemberId... ids) {
        return new LinkedHashSet<>(List.of(ids));
    }
}
