package me.aboullaite.moneytransfer.interfaces.repositories;

import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Transaction;

import java.math.BigDecimal;

public interface TransactionRepository extends Repository<Transaction> {

    Transaction add(Account debit, Account credit, BigDecimal amount);

    PagedResult<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage);

    default PagedResult<Transaction> getByAccount(Account account, Pagination pagination) {
        return getByAccount(account, pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
