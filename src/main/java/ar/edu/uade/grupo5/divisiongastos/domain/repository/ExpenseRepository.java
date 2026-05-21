package ar.edu.uade.grupo5.divisiongastos.domain.repository;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Expense;
import ar.edu.uade.grupo5.divisiongastos.domain.model.GroupId;

import java.util.List;

public interface ExpenseRepository {

    Expense save(Expense expense);

    List<Expense> findByGroupId(GroupId groupId);
}
