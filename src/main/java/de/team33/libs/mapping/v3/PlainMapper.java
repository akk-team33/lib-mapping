package de.team33.libs.mapping.v3;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlainMapper<C> implements Mapper<C> {

    public static <C> Builder<C> builder() {
        return new Builder<>();
    }

    @Override
    public final Map<String, Object> map(final C origin) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public final C remap(final Map<String, Object> origin, final C target) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder<C> {

        public final <V> Builder<C> add(final String key, final Function<C, V> getter, final BiConsumer<C, V> setter) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public final PlainMapper<C> build() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
