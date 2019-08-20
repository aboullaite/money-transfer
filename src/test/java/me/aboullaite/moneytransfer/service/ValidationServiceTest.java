package me.aboullaite.moneytransfer.service;

import me.aboullaite.moneytransfer.interfaces.Account;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationServiceTest {

    @Test
    public void testValidateAmountNotNegative() {

        assertThrows(IllegalArgumentException.class, () -> ValidationService.validateAmountNotNegative(BigDecimal.valueOf(10).negate()));

    }

    @Test
    public void testValidateAmountPositive() {

        assertThrows(IllegalArgumentException.class, () -> ValidationService.validateAmountPositive(BigDecimal.ZERO));

    }

    @Test
    public void testValidateAccountsWhenDebitNotValid() {
        final Account debit = mock(Account.class);
        final Account credit = mock(Account.class);

        when(debit.isNotValid()).thenReturn(true);
        when(credit.isNotValid()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> ValidationService.validateAccountsAreValid(debit, credit));
    }

    @Test
    public void testValidateAccountsWhenCreditNotValid() {
        final Account debit = mock(Account.class);
        final Account credit = mock(Account.class);

        when(debit.isNotValid()).thenReturn(false);
        when(credit.isNotValid()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> ValidationService.validateAccountsAreValid(debit, credit));
    }

    @Test
    public void testValidateAccountIsDifferent() {
        final Account debit = mock(Account.class);

        assertThrows(IllegalArgumentException.class, () -> ValidationService.validateAccountIsDifferent(debit, debit));

    }

    @Test
    public void testValidatePagination() {
        final int pageNumber = 0;
        final int recordsPerPage = 0;

        assertThrows(IllegalArgumentException.class, () -> ValidationService.validatePagination(pageNumber, recordsPerPage));

    }
}
