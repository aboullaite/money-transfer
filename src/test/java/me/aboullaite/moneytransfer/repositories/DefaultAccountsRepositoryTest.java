package me.aboullaite.moneytransfer.repositories;

import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.repositories.HolderRepository;
import me.aboullaite.moneytransfer.interfaces.repositories.PagedResult;
import me.aboullaite.moneytransfer.models.accounts.AbstractAccount;
import me.aboullaite.moneytransfer.models.holders.SampleHolder;
import me.aboullaite.moneytransfer.service.PagedResultImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultAccountsRepositoryTest {

    @Mock
    private HolderRepository mockHolderRepository;

    private DefaultAccountsRepository defaultAccountsRepositoryUnderTest;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        defaultAccountsRepositoryUnderTest = new DefaultAccountsRepository(mockHolderRepository);
    }

    @Test
    public void testGetById() {
        final String id = "id";
        final Account expectedResult = AbstractAccount.getInvalid();

        final Account result = defaultAccountsRepositoryUnderTest.getById(id);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddAccount() {
        final String number = "12345678901234567890";
        final Holder holder= SampleHolder.makeSampleHolder("name", "email");
        final BigDecimal balance = new BigDecimal("0.00");

        final Account result = defaultAccountsRepositoryUnderTest.addAccount(number, holder, balance);

        assertTrue(defaultAccountsRepositoryUnderTest.size() == 1);
        assertEquals(result.getBalance(), balance);
        assertEquals(result.getNumber(), number);
        assertEquals(result.getHolder(), holder);
    }

    @Test
    public void testGetByHolder() {
        // Setup
        // Setup
        final String number = "12345678901234567890";
        final Holder holder= SampleHolder.makeSampleHolder("name", "email");
        final BigDecimal balance = new BigDecimal("0.00");
        Account account = defaultAccountsRepositoryUnderTest.addAccount(number, holder, balance);

        // Run the test
        final Collection<Account> aList = defaultAccountsRepositoryUnderTest.getByHolder(holder);
        Optional<Account> result = aList.stream().findFirst();
        // Verify the results
        assertEquals(1, aList.size());
        assertEquals(number, result.get().getNumber());
        assertEquals(balance, result.get().getBalance());
        assertEquals(holder, result.get().getHolder());
    }
}
