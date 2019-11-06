package de.team33.libs.mapping.v5;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapMapper<T> implements BiMapper<T, Map<String, Object>> {

    private final List<Entry<T, ?>> entries;

    private MapMapper(final Builder<T> builder) {
        this.entries = new ArrayList<>(builder.entries);
    }

    @Override
    public final Map<String, Object> toRight(final T left) {
        final Map<String, Object> right = new TreeMap<>();
        for (final Entry<T, ?> entry : entries) {
            entry.toRight(left, right);
        }
        return right;
    }

    @Override
    public final T toLeft(final Map<String, Object> right) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder<T> {

        private final List<Entry<T, ?>> entries = new LinkedList<>();

        public final <V> Builder<T> add(final Function<T, V> getter, final BiConsumer<T, V> setter) {
            return add(UUID.randomUUID().toString(), getter, setter);
        }

        public final <V> Builder<T> add(final String key, final Function<T, V> getter, final BiConsumer<T, V> setter) {
            entries.add(new Entry<>(key, getter, setter));
            return this;
        }

        public final MapMapper<T> build() {
            return new MapMapper<>(this);
        }
    }

    private static class Entry<T, V> {

        private final String key;
        private final Function<T, V> getter;
        private final BiConsumer<T, V> setter;

        private Entry(final String key, final Function<T, V> getter, final BiConsumer<T, V> setter) {
            this.key = key;
            this.getter = getter;
            this.setter = setter;
        }

        private void toRight(final T left, final Map<String, Object> right) {
            right.put(key, getter.apply(left));
        }
    }
}
