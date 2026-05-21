package ar.edu.uade.grupo5.divisiongastos.infrastructure.persistence;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Expense;
import ar.edu.uade.grupo5.divisiongastos.domain.model.ExpenseId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;
import ar.edu.uade.grupo5.divisiongastos.domain.repository.ExpenseRepository;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {

    private final ConcurrentHashMap<ExpenseId, Expense> expenses = new ConcurrentHashMap<>();

    @Override
    public Expense save(Expense expense) {
        expenses.put(expense.id(), expense);
        return expense;
    }

    @Override
    public List<Expense> findByGroupId(GroupId groupId) {
        return expenses.values().stream()
                .filter(expense -> expense.groupId().equals(groupId))
                .sorted(Comparator.comparing(Expense::date).thenComparing(expense -> expense.description().toLowerCase()))
                .toList();
    }
}
