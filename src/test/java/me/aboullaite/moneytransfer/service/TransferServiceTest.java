package me.aboullaite.moneytransfer.service;

import me.aboullaite.moneytransfer.enums.TxStatus;
import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.Transaction;
import me.aboullaite.moneytransfer.interfaces.repositories.TransactionRepository;
import me.aboullaite.moneytransfer.utils.TransactionPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransferServiceTest {

    private TransferService transferServiceUnderTest;

    @BeforeEach
    public void setUp() {
        transferServiceUnderTest = TransferService.getInstance();
    }


    @Test
    public void testTransfer() {
        final Holder debitH = mock(Holder.class);
        final Holder creditH = mock(Holder.class);
        final BigDecimal amount = new BigDecimal("10.00");

        Account debit = transferServiceUnderTest.getContext().getAccountsRepository().addAccount("12345678901234567890", debitH, amount);
        Account credit = transferServiceUnderTest.getContext().getAccountsRepository().addAccount("12345678901234567891", creditH, amount);
        TransactionPayload payload = new TransactionPayload(debit.getId(), credit.getId(), amount);
        TransactionRepository txRepo = spy(TransactionRepository.class);

        final Transaction result = transferServiceUnderTest.transfer(payload);

        assertEquals(debit, result.getDebit());
        assertEquals(credit, result.getCredit());
        assertEquals(amount, result.getAmount());
        assertEquals(TxStatus.COMPLETED, result.getStatus());
    }

}
