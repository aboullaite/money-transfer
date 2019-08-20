package me.aboullaite.moneytransfer.interfaces;


public interface Validatable {

    boolean isValid();

    default boolean isNotValid() {
        return !isValid();
    }
}