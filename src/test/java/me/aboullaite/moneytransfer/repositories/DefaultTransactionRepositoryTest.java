package me.aboullaite.moneytransfer.repositories;

import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Transaction;
import me.aboullaite.moneytransfer.interfaces.repositories.PagedResult;
import me.aboullaite.moneytransfer.models.transactions.AbstractTransaction;
import me.aboullaite.moneytransfer.models.transactions.MoneyTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static  org.mockito.Mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultTransactionRepositoryTest {

    private DefaultTransactionRepository defaultTransactionRepositoryUnderTest;

    @BeforeEach
    public void setUp() {
        defaultTransactionRepositoryUnderTest = new DefaultTransactionRepository();
    }

    @Test
    public void testGetById() {
        final String id = "id";
        final Transaction expectedResult = MoneyTransaction.getInvalid();

        final Transaction result = defaultTransactionRepositoryUnderTest.getById(id);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAdd() {
        final Account debit = mock(Account.class);
        final Account credit = mock(Account.class);
        final BigDecimal amount = new BigDecimal("10.00");

        final Transaction result = defaultTransactionRepositoryUnderTest.add(debit, credit, amount);

        assertEquals(debit, result.getDebit());
        assertEquals(credit, result.getCredit());
        assertEquals(amount, result.getAmount());
    }
}
