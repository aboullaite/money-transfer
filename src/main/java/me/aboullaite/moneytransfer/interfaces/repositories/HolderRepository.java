package me.aboullaite.moneytransfer.interfaces.repositories;

import me.aboullaite.moneytransfer.interfaces.Holder;

public interface HolderRepository extends Repository<Holder> {

    Holder addHolder(String name, String email);
}
