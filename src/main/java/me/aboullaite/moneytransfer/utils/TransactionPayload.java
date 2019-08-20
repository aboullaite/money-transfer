
package me.aboullaite.moneytransfer.utils;

import java.math.BigDecimal;

public final class TransactionPayload {

    private final String debitAccountId;
    private final String creditAccountId;
    private final BigDecimal amount;

    public TransactionPayload(String debitAccountId, String creditAccountId, BigDecimal amount) {
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.amount = amount;
    }

    public String getDebitAccountId() {
        return debitAccountId;
    }

    public String getCreditAccountId() {
        return creditAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
