package me.aboullaite.moneytransfer.models.transactions;

import me.aboullaite.moneytransfer.enums.TxStatus;
import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Transaction;
import me.aboullaite.moneytransfer.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class MoneyTransaction extends AbstractTransaction {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransaction.class);
    public static final long ACCOUNT_WAIT_INTERVAL = 10L; // in milliseconds

    MoneyTransaction( Account debit, Account credit, BigDecimal amount) {
       super( debit, credit, amount);
        ValidationService.validateAmountPositive(amount);
        ValidationService.validateAccountsAreValid(debit, credit);
        ValidationService.validateAccountIsDifferent(debit, credit);
    }

    @Override
    public synchronized boolean run() {
        if (getStatus() != TxStatus.COMPLETED) {
            changeState();
            return doRun();
        }
        return false;
    }

    private boolean doRun() {
        final Lock debitLock = getDebit().writeLock();
        try {
            if (debitLock.tryLock(ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    final Lock creditLock = getCredit().writeLock();
                    if (creditLock.tryLock(ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                        try {
                            if (getDebit().debit(getAmount())) {
                                if (getCredit().credit(getAmount())) {
                                    setState(TxStatus.COMPLETED);
                                    logger.trace("Transaction {} completed", getId());
                                    return true;
                                }
                            }
                            setState(TxStatus.INSUFFICIENT_FUNDS);
                        } finally {
                            creditLock.unlock();
                        }
                    } else {
                        setState(TxStatus.CONCURRENCY_ERROR);
                    }
                } finally {
                    debitLock.unlock();
                }
            } else {
                setState(TxStatus.CONCURRENCY_ERROR);
            }
        } catch (InterruptedException e) {
            setState(TxStatus.CONCURRENCY_ERROR);
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private void changeState() {
        switch (getStatus()) {
            case INSUFFICIENT_FUNDS:
            case CONCURRENCY_ERROR:
                setState(TxStatus.RESTARTED);
                break;
        }
    }

    public static Transaction getInvalid() {
        return InvalidTransaction.getInstance();
    }

    public static Transaction make( Account debit, Account credit, BigDecimal amount) {
        return new MoneyTransaction(debit, credit, amount);
    }
}
