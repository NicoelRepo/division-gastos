package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.domain.model.Expense;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Group;
import ar.edu.uade.grupo5.divisiongastos.domain.model.MemberId;
import ar.edu.uade.grupo5.divisiongastos.domain.model.Transfer;

import org.springframework.stereotype.Component;

@Component
public class GroupViewMapper {

    public ExpenseView toExpenseView(Group group, Expense expense) {
        return new ExpenseView(
                expense.description(),
                expense.amount(),
                expense.date(),
                memberName(group, expense.payerId()),
                expense.participants().stream().map(memberId -> memberName(group, memberId)).toList()
        );
    }

    public TransferView toTransferView(Group group, Transfer transfer) {
        return new TransferView(
                memberName(group, transfer.from()),
                memberName(group, transfer.to()),
                transfer.amount()
        );
    }

    private String memberName(Group group, MemberId memberId) {
        return group.findMember(memberId)
                .map(member -> member.name())
                .orElse("Miembro eliminado");
    }
}
