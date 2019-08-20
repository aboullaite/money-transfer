package me.aboullaite.moneytransfer.utils.generators;

import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.repositories.HolderRepository;
import me.aboullaite.moneytransfer.repositories.Context;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class RandomHolderGenerator extends AbstractGenerator {

    private final int holdersCount;

    RandomHolderGenerator(final Context context, final int holdersCount) {
        super(context, "holders");
        this.holdersCount = holdersCount;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(holdersCount);
        for (int i = 0; i < holdersCount; ++i) {
            futures.add(threadPool.submit(this::generateHolder));
        }
        return futures;
    }

    private void generateHolder() {
        final HolderRepository holderRepository = context.getHolderRepository();
        final String name = generateName();
        Holder holder = holderRepository.addHolder( name, name+ "@email.com");
        ids.add(holder.getId());

    }

    private static String generateName() {
        return RandomStringUtils.randomAlphabetic(20);
    }

}
