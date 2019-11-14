package de.team33.libs.mapping.v9;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PropertyMapping<T> {

    private final Map<String, Entry<T, ?>> entries = new TreeMap<>();
    private final Supplier<Map<String, Object>> newResult = TreeMap::new; // TODO
    private final Supplier<T> newOrigin;

    public PropertyMapping(final Supplier<T> newOrigin) {
        this.newOrigin = newOrigin;
    }

    public final <V> PropertyMapping<T> add(final String name,
                                            final Function<T, V> getter,
                                            final BiConsumer<T, V> setter) {
        entries.put(name, new Entry<>(getter, setter));
        return this;
    }

    public final Codec<T, Map<String, Object>> codec() {
        return new Forward<>(this);
    }

    private static final class Entry<T, V> {

        private final Function<T, V> getter;
        private final BiConsumer<T, V> setter;

        private Entry(final Function<T, V> getter, final BiConsumer<T, V> setter) {
            this.getter = getter;
            this.setter = setter;
        }

        private void forward(final String name, final T origin, final Map<String, Object> result) {
            result.put(name, getter.apply(origin));
        }

        @SuppressWarnings("unchecked")
        private void revert(final String name, final Map<String, Object> origin, final T result) {
            setter.accept(result, (V) origin.get(name));
        }
    }

    private static class Forward<T> implements Codec<T, Map<String, Object>> {

        private final Map<String, Entry<T, ?>> entries;
        private final Codec<Map<String, Object>, T> reverse;
        private final Supplier<Map<String, Object>> newResult;

        private Forward(final PropertyMapping<T> builder) {
            this.entries = new TreeMap<>(builder.entries);
            this.newResult = builder.newResult;
            this.reverse = new Reverse<>(this, entries, builder.newOrigin);
        }

        @Override
        public final Map<String, Object> apply(final T origin) {
            final Map<String, Object> result = newResult.get();
            entries.forEach((name, entry) -> entry.forward(name, origin, result));
            return result;
        }

        @Override
        public final Codec<Map<String, Object>, T> reversal() {
            return this.reverse;
        }
    }

    private static class Reverse<T> implements Codec<Map<String, Object>, T> {

        private final Forward<T> forward;
        private final Map<String, Entry<T, ?>> entries;
        private final Supplier<T> newResult;

        private Reverse(final Forward<T> forward, final Map<String, Entry<T, ?>> entries, final Supplier<T> newResult) {
            this.forward = forward;
            this.entries = entries;
            this.newResult = newResult;
        }

        @Override
        public final T apply(final Map<String, Object> origin) {
            final T result = newResult.get();
            entries.forEach((name, entry) -> entry.revert(name, origin, result));
            return result;
        }

        @Override
        public final Codec<T, Map<String, Object>> reversal() {
            return forward;
        }
    }
}
