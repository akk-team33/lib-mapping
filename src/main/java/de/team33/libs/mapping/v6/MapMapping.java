package de.team33.libs.mapping.v6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapMapping<T> {

    private final List<Entry<T, ?>> entries = new LinkedList<>();
    private final Supplier<T> newOrigin;

    private Supplier<Map<String, Object>> newResult;

    public MapMapping(final Supplier<T> newOrigin) {
        this.newOrigin = newOrigin;
        this.newResult = TreeMap::new;
    }

    public final MapMapping<T> setNewResult(final Supplier<Map<String, Object>> newResult) {
        this.newResult = newResult;
        return this;
    }

    public final <V> MapMapping<T> add(final String key, final Function<T, V> getter, final BiConsumer<T, V> setter) {
        entries.add(new Entry<>(key, getter, setter));
        return this;
    }

    public final Mapper<T, Map<String, Object>> mapper() {
        return new Forward<>(this);
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

    private static final class Forward<T> implements Mapper<T, Map<String, Object>> {

        private final List<Entry<T, ?>> entries;
        private final Supplier<Map<String, Object>> newResult;
        private final Mapper<Map<String, Object>, T> reverse;

        private Forward(final MapMapping<T> builder) {
            this.entries = new ArrayList<>(builder.entries);
            this.newResult = builder.newResult;
            this.reverse = new Reverse(builder.newOrigin);
        }

        @Override
        public final Map<String, Object> apply(final T origin) {
            final Map<String, Object> result = newResult.get();
            for (final Entry<T, ?> entry : entries) {
                entry.forward(origin, result);
            }
            return result;
        }

        @Override
        public final Mapper<Map<String, Object>, T> reversal() {
            return reverse;
        }

        private final class Reverse implements Mapper<Map<String, Object>, T> {

            private final Supplier<T> newOrigin;

            private Reverse(final Supplier<T> newOrigin) {
                this.newOrigin = newOrigin;
            }

            @Override
            public T apply(final Map<String, Object> origin) {
                final T result = newOrigin.get();
                for (final Entry<T, ?> entry : entries) {
                    entry.reverse(origin, result);
                }
                return result;
            }

            @Override
            public Mapper<T, Map<String, Object>> reversal() {
                return Forward.this;
            }
        }
    }
}
