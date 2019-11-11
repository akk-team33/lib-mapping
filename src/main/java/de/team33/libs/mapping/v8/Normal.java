package de.team33.libs.mapping.v8;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Normal {

    Normal() {
    }

    @SuppressWarnings({"DesignForExtension", "MethodMayBeStatic"})
    public Optional<String> asSimple() {
        return Optional.empty();
    }

    @SuppressWarnings({"DesignForExtension", "MethodMayBeStatic"})
    public Optional<List<Normal>> asSequence() {
        return Optional.empty();
    }

    @SuppressWarnings({"DesignForExtension", "MethodMayBeStatic"})
    public Optional<Map<Normal, Normal>> asComplex() {
        return Optional.empty();
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract String toString();
}
