package me.aboullaite.moneytransfer.models.holders;

import me.aboullaite.moneytransfer.interfaces.Base;

final class InvalidHolder extends AbstractHolder {

    private InvalidHolder() {
        super(Base.INVALID_ID, "", "");
    }

    @Override
    public String getName() {
        return "";
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

        return (obj instanceof InvalidHolder);
    }

    private static class LazyHolder {
        private static final InvalidHolder INSTANCE = new InvalidHolder();
    }

    static InvalidHolder getInstance() {
        return LazyHolder.INSTANCE;
    }
}
