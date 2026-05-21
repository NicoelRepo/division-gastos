package ar.edu.uade.grupo5.divisiongastos.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpenseForm {

    private String payerId;
    private BigDecimal amount;
    private String description;
    private List<String> participantIds = new ArrayList<>();

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds == null ? new ArrayList<>() : participantIds;
    }
}
