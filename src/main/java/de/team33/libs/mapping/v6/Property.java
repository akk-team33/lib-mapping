package de.team33.libs.mapping.v6;

public interface Property<S, V> {

    V get(S subject);

    void set(S subject, V value);
}
