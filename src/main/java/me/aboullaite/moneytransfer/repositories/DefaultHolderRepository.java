package me.aboullaite.moneytransfer.repositories;

import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.repositories.HolderRepository;
import me.aboullaite.moneytransfer.interfaces.repositories.PagedResult;
import me.aboullaite.moneytransfer.models.holders.AbstractHolder;
import me.aboullaite.moneytransfer.service.PagedResultImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class DefaultHolderRepository implements HolderRepository {

    private final ConcurrentMap<String, Holder> holders;

    DefaultHolderRepository() {
        holders = new ConcurrentHashMap<>();
    }


    @Override
    public Holder getById(String id) {
        return holders.getOrDefault(id, getInvalid());
    }

    @Override
    public Holder getInvalid() {
        return AbstractHolder.getInvalid();
    }

    @Override
    public Holder addHolder(String name, String email) {
        final Holder holder= AbstractHolder.makeSampleHolder( name, email);
        holders.putIfAbsent(holder.getId(), holder);
        return holder;
    }

    @Override
    public PagedResult<Holder> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultImpl.from(pageNumber, recordsPerPage, holders);
    }

    @Override
    public int size() {
        return holders.size();
    }
}
