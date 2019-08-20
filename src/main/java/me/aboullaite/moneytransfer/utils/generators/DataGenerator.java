package me.aboullaite.moneytransfer.utils.generators;

import me.aboullaite.moneytransfer.repositories.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    private final Context context;
    private int holdersCount = 100_000;
    private int accountsPerClient = 10;
    private boolean initialTransactions = true;
    private boolean runImmediately = true;
    private int clientTransactionsCount = 1_000_000;

    private DataGenerator(Context context) {
        this.context = context;
    }

    public DataGenerator withHoldersCount(final int count) {
        holdersCount = count;
        return this;
    }

    public DataGenerator withAccountsPerClient(final int count) {
        this.accountsPerClient = count;
        return this;
    }

    public DataGenerator withClientTransactions(final int count) {
        this.clientTransactionsCount = count;
        if (count > 0) {
            initialTransactions = true;
        }
        return this;
    }

    public DataGenerator withoutClientTransactions() {
        this.clientTransactionsCount = 0;
        return this;
    }

    public void generate() {
        try {
            final List<String> holderIds = generateHolders();
            final List<String> accountIds = generateAccounts(holderIds);
            if (initialTransactions) {
                generateInitialTransactions(accountIds);
                if (clientTransactionsCount > 0) {
                    generateClientTransactions(accountIds);
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private List<String> generateHolders() {
        final AbstractGenerator holderGenerator = new RandomHolderGenerator(context, holdersCount);
        final List<String> holderIds = holderGenerator.generate();
        logger.debug("Holder ids count = {}", holderIds.size());
        logger.debug("Holder repository size = {}", context.getHolderRepository().size());
        return holderIds;
    }

    private List<String> generateAccounts(final List<String> holderIds) {
        final AbstractGenerator accountGenerator = new RandomAccountGenerator(context, holderIds, accountsPerClient);
        final List<String> accountIds = accountGenerator.generate();
        logger.debug("Account ids count = {}", accountIds.size());
        logger.debug("Account repository size = {}", context.getAccountsRepository().size());
        return accountIds;
    }

    private void generateInitialTransactions(final List<String> accountIds) {
        final AbstractGenerator initialTransactionGenerator = new InitialTransactionGenerator(context, accountIds, runImmediately);
        final List<String> initialTrnIds = initialTransactionGenerator.generate();
        logger.debug("Initial transaction ids count = {}", initialTrnIds.size());
        logger.debug("Transaction repository size = {}", context.getTransactionRepository().size());
    }

    private void generateClientTransactions(final List<String> accountIds) {
        final AbstractGenerator transactionGenerator = new RandomTransactionGenerator(
                context, accountIds, runImmediately, 10, clientTransactionsCount);
        final List<String> trnIds = transactionGenerator.generate();
        logger.debug("Transaction ids count = {}", trnIds.size());
        logger.debug("Transaction repository size = {}", context.getTransactionRepository().size());
    }

    public static DataGenerator getInstance(Context context) {
        return new DataGenerator(context);
    }
}
