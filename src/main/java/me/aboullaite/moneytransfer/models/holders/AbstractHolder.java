package me.aboullaite.moneytransfer.models.holders;
import me.aboullaite.moneytransfer.interfaces.Holder;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractHolder implements Holder {

    private final String id;
    private final String name;
    private final String email;

    AbstractHolder(String id, String name, String email) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "email cannot be null");

        this.id = id;
        this.name = name;
        this.email = email;
    }

    AbstractHolder(String name, String email) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "email cannot be null");

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public static Holder makeSampleHolder(String name, String email) {
        return new SampleHolder(name, email);
    }

    public static Holder getInvalid() {
        return InvalidHolder.getInstance();
    }

}
