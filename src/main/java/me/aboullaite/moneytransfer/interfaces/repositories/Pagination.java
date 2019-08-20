package me.aboullaite.moneytransfer.interfaces.repositories;

import me.aboullaite.moneytransfer.interfaces.Validatable;

public interface Pagination extends Validatable {

    int getRecordsPerPage();

    int getPageNumber();
}
