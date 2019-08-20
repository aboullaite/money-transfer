package me.aboullaite.moneytransfer.models.accounts;

import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.models.holders.SampleHolder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SampleAccountTest {

    private final String number = "12345678901234567890";
    private final Holder holder = SampleHolder.makeSampleHolder("user", "user@email.com");
    private final BigDecimal zeroBalance = BigDecimal.ZERO;
    private final BigDecimal balance = new BigDecimal("10.00");

    @Test
    public void testMakeActiveBalance() {

        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder, balance);

        assertEquals(sa.getNumber(), number);
        assertEquals(sa.getHolder(), holder);
        assertEquals(sa.getBalance(), balance);
        assertTrue(sa.isActive());

    }

    @Test
    public void testMakeActiveBalancewithZero() {

        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder);

        assertEquals(sa.getNumber(), number);
        assertEquals(sa.getHolder(), holder);
        assertEquals(sa.getBalance(), zeroBalance);
        assertTrue(sa.isActive());
    }

    @Test
    public void testMakePassiveBalance() {
        final SampleAccount sa = SampleAccount.makePassiveBalance(number, holder);

        assertEquals(sa.getNumber(), number);
        assertEquals(sa.getHolder(), holder);
        assertEquals(sa.getBalance(), zeroBalance);
        assertFalse(sa.isActive());
    }

    @Test
    public void testDebit() {
        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder, balance);

        boolean d = sa.debit(balance);

        assertTrue(d);
    }

    @Test
    public void testDebitWithZeroBalance() {
        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder, zeroBalance);

        boolean d = sa.debit(balance);

        assertFalse(d);
    }

    @Test
    public void testDebitWithNegativeBalance() {
        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder, balance);

        assertThrows(IllegalArgumentException.class, () -> sa.debit(balance.negate()));
    }

    @Test
    public void testCredit() {
        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder, balance);

        // Run the test
        boolean d = sa.credit(balance);
        // Verify the results
        assertTrue(d);
    }

    @Test
    public void testCreditWithNegativeBalance() {
        final SampleAccount sa = SampleAccount.makeActiveBalance(number, holder, balance);

        assertThrows(IllegalArgumentException.class, () -> sa.credit(balance.negate()));
    }
}
