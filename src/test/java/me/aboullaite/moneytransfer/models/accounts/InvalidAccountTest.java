package me.aboullaite.moneytransfer.models.accounts;

import me.aboullaite.moneytransfer.interfaces.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvalidAccountTest {

    private Account invalidAccountUnderTest;

    @BeforeEach
    public void setUp() {
        invalidAccountUnderTest = InvalidAccount.getInstance();
    }

    @Test
    public void testHashCode() {
        final int expectedResult = "".hashCode();

        final int result = invalidAccountUnderTest.hashCode();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testEquals() {
        final Object nullObj = null;
        final Object obj = InvalidAccount.getInstance();

        assertNotNull(invalidAccountUnderTest);
        assertTrue(invalidAccountUnderTest.equals(obj));
        assertFalse(invalidAccountUnderTest.equals(nullObj));
    }

    @Test
    public void testToString() {

        assertTrue(invalidAccountUnderTest.toString().startsWith("InvalidAccount{"));

    }
}
