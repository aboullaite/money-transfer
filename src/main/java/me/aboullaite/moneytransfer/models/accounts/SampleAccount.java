package me.aboullaite.moneytransfer.models.accounts;

import me.aboullaite.moneytransfer.interfaces.Holder;

import java.math.BigDecimal;

public final class SampleAccount extends AbstractAccount {

    private SampleAccount(String number, Holder holder, boolean active, BigDecimal balance) {
        super(number, holder, active, balance);
        validateNumber(number);
    }

    private SampleAccount(String number, Holder holder, boolean active) {
        this(number, holder, active, BigDecimal.ZERO);
    }

    private static void validateNumber(String number) {
        if (number.length() != 20) {
            throw new IllegalArgumentException("SampleAccount number must contain 20 characters");
        }
    }

    public static SampleAccount makeActiveBalance(String number, Holder holder, BigDecimal balance) {
        return new SampleAccount(number, holder, true, balance);
    }

    public static SampleAccount makeActiveBalance(String number, Holder holder) {
        return new SampleAccount(number, holder, true);
    }

    public static SampleAccount makePassiveBalance(String number, Holder holder) {
        return new SampleAccount(number, holder, false);
    }
}
