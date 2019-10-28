package de.team33.libs.mapping.v4.uni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class Sequential<S, R> implements Function<S, R> {

    private final Collection<Entry<S, R, ?>> entries;
    private final Supplier<R> newResult;

    private Sequential(final Builder<S, R> builder) {
        this.entries = new ArrayList<>(builder.entries);
        this.newResult = builder.newResult;
    }

    @Override
    public final R apply(final S source) {
        final R result = newResult.get();
        for (final Entry<S, R, ?> entry : entries) {
            entry.map(source, result);
        }
        return result;
    }

    public static class Builder<S, R> {

        private final Collection<Entry<S, R, ?>> entries = new LinkedList<>();
        private final Supplier<R> newResult;

        public Builder(final Supplier<R> newResult) {
            this.newResult = newResult;
        }

        public final <V> Builder<S, R> add(final Function<S, V> getter, final BiConsumer<R, V> setter) {
            entries.add(new Entry<>(getter, setter));
            return this;
        }

        public final Sequential<S, R> build() {
            return new Sequential<>(this);
        }
    }

    private static final class Entry<S, R, V> {

        private final Function<S, V> getter;
        private final BiConsumer<R, V> setter;

        private Entry(final Function<S, V> getter, final BiConsumer<R, V> setter) {
            this.getter = getter;
            this.setter = setter;
        }

        public void map(final S source, final R result) {
            setter.accept(result, getter.apply(source));
        }
    }
}
