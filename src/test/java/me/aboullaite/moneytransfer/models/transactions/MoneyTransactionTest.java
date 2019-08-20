package me.aboullaite.moneytransfer.models.transactions;

import me.aboullaite.moneytransfer.enums.TxStatus;
import me.aboullaite.moneytransfer.interfaces.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MoneyTransactionTest {

    @Mock(answer= Answers.RETURNS_DEEP_STUBS)
    private Account mockDebit;
    @Mock(answer= Answers.RETURNS_DEEP_STUBS)
    private Account mockCredit;

    private MoneyTransaction moneyTransactionUnderTest;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        moneyTransactionUnderTest = new MoneyTransaction(mockDebit, mockCredit, new BigDecimal("10.00"));
    }

    @Test
    public void testRun() throws InterruptedException{
        when(mockDebit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(true);
        when(mockCredit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(true);
        when(mockDebit.debit(any())).thenReturn(true);
        when(mockCredit.credit(any())).thenReturn(true);

        final boolean result = moneyTransactionUnderTest.run();

        assertTrue(result);
        assertEquals(moneyTransactionUnderTest.getStatus(), TxStatus.COMPLETED);
    }

    @Test
    public void testRunWithDebitFalse() throws InterruptedException{
        when(mockDebit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(true);
        when(mockCredit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(true);
        when(mockDebit.debit(any())).thenReturn(false);

        final boolean result = moneyTransactionUnderTest.run();

        assertFalse(result);
        assertEquals(moneyTransactionUnderTest.getStatus(), TxStatus.INSUFFICIENT_FUNDS);
    }

    @Test
    public void testRunWithCreditFalse() throws InterruptedException{

        when(mockDebit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(true);
        when(mockCredit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(true);
        when(mockDebit.debit(any())).thenReturn(true);
        when(mockCredit.credit(any())).thenReturn(false);

        final boolean result = moneyTransactionUnderTest.run();

        assertFalse(result);
        assertEquals(moneyTransactionUnderTest.getStatus(), TxStatus.INSUFFICIENT_FUNDS);
    }

    @Test
    public void testRunWithLockIssue() throws InterruptedException{

        when(mockDebit.writeLock().tryLock(moneyTransactionUnderTest.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)).thenReturn(false);

        final boolean result = moneyTransactionUnderTest.run();


        assertFalse(result);
        assertEquals(moneyTransactionUnderTest.getStatus(), TxStatus.CONCURRENCY_ERROR);
    }
}
