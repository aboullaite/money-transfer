package me.aboullaite.moneytransfer.service;

import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Transaction;
import me.aboullaite.moneytransfer.interfaces.repositories.Repository;
import me.aboullaite.moneytransfer.repositories.Context;
import me.aboullaite.moneytransfer.utils.TransactionPayload;
import me.aboullaite.moneytransfer.utils.generators.DataGenerator;

import java.util.NoSuchElementException;
import java.util.Objects;

public final class TransferService {

    private final Context context;

    private TransferService(Context context) {
        this.context = context;
    }

    public void generateData() {
        DataGenerator.getInstance(context)
                .withHoldersCount(100)
                .withAccountsPerClient(2)
                .withClientTransactions(10_000)
                .generate();
    }

    public Transaction transfer(TransactionPayload payload) {
        Objects.requireNonNull(payload, "Transaction data cannot be null");

        final Repository<Account> accountRepository = context.getAccountsRepository();
        final Account debit = accountRepository.getById(payload.getDebitAccountId());
        validateAccount(debit, payload.getDebitAccountId());
        final Account credit = accountRepository.getById(payload.getCreditAccountId());
        validateAccount(credit, payload.getCreditAccountId());

        final Transaction trn = context.getTransactionRepository().add(debit, credit, payload.getAmount());
        trn.run();
        return trn;
    }

    private void validateAccount(Account account, String id) {
        if (account.isNotValid()) {
            throw new NoSuchElementException(String.format("Account with id %s is not found", id));
        }
    }

    private static class LazyHolder {
        static final TransferService INSTANCE = new TransferService(Context.create());
    }

    public static TransferService getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Context getContext() {
        return context;
    }
}
