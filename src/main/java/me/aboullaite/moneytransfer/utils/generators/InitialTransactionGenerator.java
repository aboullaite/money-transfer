package me.aboullaite.moneytransfer.utils.generators;

import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.Transaction;
import me.aboullaite.moneytransfer.interfaces.repositories.AccountsRepository;
import me.aboullaite.moneytransfer.repositories.Context;
import me.aboullaite.moneytransfer.utils.TransactionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class InitialTransactionGenerator extends AbstractGenerator {

    private final List<String> accountIds;
    private final boolean runImmediately;
    private final Account debitAccount;

    InitialTransactionGenerator(Context context, List<String> accountIds, boolean runImmediately) {
        super(context, "initial transactions");
        Objects.requireNonNull(accountIds, "Ids list cannot be null");
        this.accountIds = accountIds;
        this.runImmediately = runImmediately;
        debitAccount  = createDebitAccount();
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(accountIds.size());
        for (String accountId : accountIds) {
            Runnable runnableTask = () -> this.generateInitialTransaction(accountId);
            futures.add(threadPool.submit(runnableTask));
        }
        return futures;
    }

    private void generateInitialTransaction(final String creditAccountId) {
        final AccountsRepository accountsRepository = context.getAccountsRepository();
        final Account credit = accountsRepository.getById(creditAccountId);
        if (credit.isValid()) {
            final BigDecimal amount = TransactionUtils.generateAmount(500_000, 1000_000);
            final Transaction transaction = context.getTransactionRepository().add(debitAccount, credit, amount);
            if (runImmediately) {
                transaction.run();
            }
            ids.add(transaction.getId());
        } else {
            logger.error("Credit account with id = {} not found", creditAccountId);
        }
    }

    private Account createDebitAccount(){
        final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(100_000_000.00d);
        final Holder someHolder = this.context.getHolderRepository().addHolder("Some User", "some.user@email.com");
        final Account someAccount = this.context.getAccountsRepository().addAccount("11111111110202020202",someHolder, INITIAL_BALANCE);

        return someAccount;
    }
}
