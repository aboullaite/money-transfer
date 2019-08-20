package me.aboullaite.moneytransfer.interfaces.repositories;

import java.util.Collection;

public interface PagedResult<T> {

    boolean hasMore();

    Collection<T> getContent();

    int getPageNumber();

    int getRecordsPerPage();
}
