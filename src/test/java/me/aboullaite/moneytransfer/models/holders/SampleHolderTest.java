package me.aboullaite.moneytransfer.models.holders;

import me.aboullaite.moneytransfer.interfaces.Holder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SampleHolderTest {

    @Test
    public void testMakeHolder(){
        Holder h = SampleHolder.makeSampleHolder("name", "email");

        assertEquals(h.getName(), "name");
        assertEquals(h.getEmail(), "email");
        assertFalse(h.isNotValid());
    }

    @Test
    public void testToString() {
        Holder h = SampleHolder.makeSampleHolder("name", "email");

        assertTrue(h.toString().startsWith("Holder{"));

    }
}
