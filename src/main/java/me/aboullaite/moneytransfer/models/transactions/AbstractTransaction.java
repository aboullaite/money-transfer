package me.aboullaite.moneytransfer.models.transactions;

import me.aboullaite.moneytransfer.enums.TxStatus;
import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractTransaction implements Transaction {

    private final String id;
    private final Account debit;
    private final Account credit;
    private final BigDecimal amount;
    private TxStatus state;
    private LocalDateTime txDate;

    public AbstractTransaction(String id, Account debit, Account credit, BigDecimal amount) {

        this.id = id;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.state = TxStatus.NEW;
        this.txDate = LocalDateTime.now();

        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(debit, "Debit account cannot be null");
        Objects.requireNonNull(credit, "Credit account cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
    }

    public AbstractTransaction(Account debit, Account credit, BigDecimal amount) {

        this.id = UUID.randomUUID().toString();
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.state = TxStatus.NEW;
        this.txDate = LocalDateTime.now();

        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(debit, "Debit account cannot be null");
        Objects.requireNonNull(credit, "Credit account cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Account getDebit() {
        return debit;
    }

    @Override
    public Account getCredit() {
        return credit;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public synchronized TxStatus getStatus() {
        return state;
    }

    @Override
    public LocalDateTime getTxDate() {
        return txDate;
    }

    public void setState(TxStatus state) {
        this.state = state;
    }
}
