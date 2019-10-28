package de.team33.libs.mapping.v4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class PlainMapper<S, R> implements Mapper<S, R> {

    @SuppressWarnings("rawtypes")
    private final List<Entry> entries;
    private final Supplier<R> newResult;

    private PlainMapper(final Builder<S, R> builder) {
        this.entries = new ArrayList<>(builder.entries);
        this.newResult = builder.newResult;
    }

    public static <S, R> Builder<S, R> builder(final Supplier<R> newResult) {
        return new Builder<>(newResult);
    }

    @Override
    public final R map(final S source) {
        final R result = newResult.get();
        //noinspection rawtypes
        for (final Entry entry : entries) {
            //noinspection unchecked
            entry.map(source, result);
        }
        return result;
    }

    public static final class Builder<S, R> {

        @SuppressWarnings("rawtypes")
        private final List<Entry> entries = new LinkedList<>();
        private final Supplier<R> newResult;

        private Builder(final Supplier<R> newResult) {
            this.newResult = newResult;
        }

        public final <V> Builder<S, R> add(final Function<S, V> getter, final BiConsumer<R, ? super V> setter) {
            entries.add(new Entry<>(getter, setter));
            return this;
        }

        public final Mapper<S, R> build() {
            return new PlainMapper<>(this);
        }
    }

    private static final class Entry<S, R, V> {

        private final Function<? super S, ? extends V> getter;
        private final BiConsumer<? super R, ? super V> setter;

        private Entry(final Function<? super S, ? extends V> getter, final BiConsumer<? super R, ? super V> setter) {
            this.getter = getter;
            this.setter = setter;
        }

        private void map(final S source, final R result) {
            setter.accept(result, getter.apply(source));
        }
    }
}
