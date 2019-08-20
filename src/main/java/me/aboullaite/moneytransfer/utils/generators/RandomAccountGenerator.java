package me.aboullaite.moneytransfer.utils.generators;

import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.repositories.AccountsRepository;
import me.aboullaite.moneytransfer.repositories.Context;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class RandomAccountGenerator extends AbstractGenerator {

    private final int accountsPerClient;
    private final List<String> holderIds;

    RandomAccountGenerator(final Context context, final List<String> holderIds, final int accountsPerClient) {
        super(context, "accounts");
        Objects.requireNonNull(holderIds, "Ids list cannot be null");
        this.holderIds = holderIds;
        this.accountsPerClient = accountsPerClient;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(holderIds.size() * accountsPerClient);
        for (String holderId : holderIds) {
            Runnable runnableTask = () -> this.generateAccount(holderId);
            futures.add(threadPool.submit(runnableTask));
        }
        return futures;
    }

    private void generateAccount(String holderId) {
        final AccountsRepository accountsRepository = context.getAccountsRepository();
        final Holder holder = context.getHolderRepository().getById(holderId);
        if (holder.isValid()) {
            for (int i = 0; i < accountsPerClient; ++i) {
                final int idx = counter.incrementAndGet();
                final Account a = accountsRepository.addAccount(generateNumber(idx), holder, BigDecimal.ZERO);
                ids.add(a.getId());
            }
        } else {
            logger.error("Holder with id = {} not found", holderId);
        }
    }

    private static String generateNumber(final int idx) {
        return "1010101010" + StringUtils.leftPad(String.valueOf(idx), 10, '0');
    }
}
