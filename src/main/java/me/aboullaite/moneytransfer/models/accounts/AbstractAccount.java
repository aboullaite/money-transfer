package me.aboullaite.moneytransfer.models.accounts;
import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractAccount implements Account {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAccount.class);
    public static final long ACCOUNT_WAIT_INTERVAL = 10L; // in milliseconds

    private final String id;
    private final String number;
    private final Holder holder;
    private final boolean active;
    private BigDecimal balance;
    private final transient Lock lock;

    AbstractAccount(String id, String number,
                    Holder holder, boolean active, BigDecimal balance) {
        Objects.requireNonNull(number, "Number cannot be null");
        Objects.requireNonNull(holder, "Holder cannot be null");
        Objects.requireNonNull(balance, "Balance cannot be null");
        ValidationService.validateAmountNotNegative(balance);

        this.id = id;
        this.number = number;
        this.holder = holder;
        this.active = active;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public AbstractAccount(String number,
                           Holder holder, boolean active, BigDecimal balance) {
        Objects.requireNonNull(number, "Number cannot be null");
        Objects.requireNonNull(holder, "Holder cannot be null");
        Objects.requireNonNull(balance, "Balance cannot be null");
        ValidationService.validateAmountNotNegative(balance);

        this.id = UUID.randomUUID().toString();;
        this.number = number;
        this.holder = holder;
        this.active = active;
        this.balance = balance;
        this.lock = new ReentrantLock();

    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final String getNumber() {
        return number;
    }


    @Override
    public final BigDecimal getBalance() {
        try {
            lock.lock();
            return balance;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean debit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        ValidationService.validateAmountNotNegative(amount);

        try {
            if (lock.tryLock(this.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    if (balance.compareTo(amount) >= 0) {
                        balance = balance.subtract(amount);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean credit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        ValidationService.validateAmountNotNegative(amount);

        try {
            if (lock.tryLock(this.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    balance = balance.add(amount);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    @Override
    public final Holder getHolder() {
        return holder;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Lock writeLock() {
        return lock;
    }

    public static Account getInvalid() {
        return InvalidAccount.getInstance();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", holder=" + holder +
                ", active=" + active +
                ", balance=" + balance +
                ", lock=" + lock +
                '}';
    }


    public static Account makeActiveAccount(String number, Holder holder, BigDecimal balance) {
        return SampleAccount.makeActiveBalance(number, holder, balance);
    }

    public static Account makeActiveAccount(String number, Holder holder) {
        return SampleAccount.makeActiveBalance(number, holder);
    }

    public static Account makePassiveAccount(String number, Holder holder) {
        return SampleAccount.makeActiveBalance(number, holder);
    }
}
