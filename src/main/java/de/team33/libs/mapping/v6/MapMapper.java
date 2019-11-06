package de.team33.libs.mapping.v6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MapMapper<T> implements Mapper<T, Map<String, Object>> {

    private final List<Entry<T, ?>> entries;
    private final Reverse reverse;

    private MapMapper(final Builder<T> builder) {
        entries = new ArrayList<>(builder.entries);
        reverse = new Reverse(builder.newOrigin);
    }

    @Override
    public final Map<String, Object> map(final T origin) {
        final Map<String, Object> result = new TreeMap<>();
        for (final Entry<T, ?> entry : entries) {
            entry.forward(origin, result);
        }
        return result;
    }

    @Override
    public final Mapper<Map<String, Object>, T> reverse() {
        return reverse;
    }

    public static final class Builder<T> {

        private final Supplier<T> newOrigin;
        private final List<Entry<T, ?>> entries = new LinkedList<>();

        public Builder(final Supplier<T> newOrigin) {
            this.newOrigin = newOrigin;
        }

        public final <V> Builder<T> add(final String key, final Function<T, V> getter, final BiConsumer<T, V> setter) {
            entries.add(new Entry<>(key, getter, setter));
            return this;
        }

        public final MapMapper<T> build() {
            return new MapMapper<>(this);
        }
    }

    private static final class Entry<T, V> {

        private final String key;
        private final Function<T, V> getter;
        private final BiConsumer<T, V> setter;

        private Entry(final String key, final Function<T, V> getter, final BiConsumer<T, V> setter) {
            this.key = key;
            this.getter = getter;
            this.setter = setter;
        }

        private void forward(final T origin, final Map<String, Object> result) {
            result.put(key, getter.apply(origin));
        }

        private void reverse(final Map<String, Object> origin, final T result) {
            //noinspection unchecked
            setter.accept(result, (V) origin.get(key));
        }
    }

    private final class Reverse implements Mapper<Map<String, Object>, T> {

        private final Supplier<T> newOrigin;

        private Reverse(final Supplier<T> newOrigin) {
            this.newOrigin = newOrigin;
        }

        @Override
        public T map(final Map<String, Object> origin) {
            final T result = newOrigin.get();
            for (final Entry<T, ?> entry : entries) {
                entry.reverse(origin, result);
            }
            return result;
        }

        @Override
        public Mapper<T, Map<String, Object>> reverse() {
            return MapMapper.this;
        }
    }
}
