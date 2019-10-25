package de.team33.libs.mapping.v3;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class PlainMapper<C> implements Mapper<C> {

    private final List<Property> properties;

    private PlainMapper(final Builder<C> builder) {
        this.properties = new ArrayList<>(builder.properties);
    }

    public static <C> Builder<C> builder() {
        return new Builder<>();
    }

    @Override
    public final Map<String, Object> map(final C origin) {
        return properties.stream()
                         .collect(toMap(property -> property.key, property -> property.getter.apply(origin)));
    }

    @Override
    public final C remap(final Map<String, Object> origin, final C target) {
        properties.stream().forEach(property -> property.setter.accept(target, origin.get(property.key)));
        return target;
    }

    public static class Builder<C> {

        private final List<Property> properties = new LinkedList<>();

        public final <V> Builder<C> add(final String key, final Function<C, V> getter, final BiConsumer<C, V> setter) {
            properties.add(new Property(key, getter, setter));
            return this;
        }

        public final PlainMapper<C> build() {
            return new PlainMapper<>(this);
        }
    }

    @SuppressWarnings("rawtypes")
    private static class Property {

        private final String key;
        private final Function getter;
        private final BiConsumer setter;

        private Property(final String key, final Function getter, final BiConsumer setter) {
            this.key = key;
            this.getter = getter;
            this.setter = setter;
        }
    }
}
