package me.aboullaite.moneytransfer.models.transactions;

import me.aboullaite.moneytransfer.interfaces.Base;
import me.aboullaite.moneytransfer.models.accounts.AbstractAccount;

import java.math.BigDecimal;

final class InvalidTransaction extends AbstractTransaction {

    private InvalidTransaction() {
        super(Base.INVALID_ID, AbstractAccount.getInvalid(), AbstractAccount.getInvalid(), BigDecimal.ZERO);
    }

    @Override
    public int hashCode() {
        return Base.INVALID_ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return (obj instanceof InvalidTransaction);
    }

    @Override
    public final boolean run() {
        return false;
    }

    private static class LazyHolder {
        private static final InvalidTransaction INSTANCE = new InvalidTransaction();
    }

    static InvalidTransaction getInstance() {
        return LazyHolder.INSTANCE;
    }
}
