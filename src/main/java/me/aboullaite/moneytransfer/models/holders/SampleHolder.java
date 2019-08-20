package me.aboullaite.moneytransfer.models.holders;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class SampleHolder extends AbstractHolder {

    SampleHolder(String name, String email) {
        super(name, email);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(39, 19)
                .append(getName())
                .append(getEmail())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        SampleHolder other = (SampleHolder) obj;
        return new EqualsBuilder()
                .append(getName(), other.getName())
                .append(getEmail(), other.getEmail())
                .isEquals();
    }
}
