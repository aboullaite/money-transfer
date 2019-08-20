package me.aboullaite.moneytransfer.models.holders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvalidHolderTest {

    private InvalidHolder invalidHolderUnderTest;

    @BeforeEach
    public void setUp() {
        invalidHolderUnderTest = InvalidHolder.getInstance();
    }


    @Test
    public void testHashCode() {
        final int expectedResult = "".hashCode();

        final int result = invalidHolderUnderTest.hashCode();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testEquals() {
        final Object nullObj = null;
        final Object obj = invalidHolderUnderTest.getInstance();

        assertNotNull(invalidHolderUnderTest);
        assertTrue(invalidHolderUnderTest.equals(obj));
        assertFalse(invalidHolderUnderTest.equals(nullObj));
    }
}
