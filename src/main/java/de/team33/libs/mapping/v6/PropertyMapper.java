package de.team33.libs.mapping.v6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PropertyMapper<O, R> implements Mapper<O, R> {

    private final List<Entry<O, ?, R, ?>> entries;
    private final Supplier<R> newResult;
    private final Mapper<R, O> reversal;

    private PropertyMapper(final Builder<O, R> builder) {
        this.entries = new ArrayList<>(builder.entries);
        this.newResult = builder.newResult;
        this.reversal = new Reversal(entries, builder.newOrigin);
    }

    @Override
    public final R apply(final O origin) {
        final R result = newResult.get();
        for (final Entry<O, ?, R, ?> entry : entries) {
            entry.forward(origin, result);
        }
        return result;
    }

    @Override
    public final Mapper<R, O> reversal() {
        return reversal;
    }

    public static class Builder<O, R> {

        private static final Mapper DIRECT = new Mapper() {
            @Override
            public Object apply(final Object origin) {
                return origin;
            }

            @Override
            public Mapper reversal() {
                return this;
            }
        };

        private final List<Entry<O, ?, R, ?>> entries = new LinkedList<>();
        private final Supplier<R> newResult;
        private final Supplier<O> newOrigin;

        public Builder(final Supplier<O> newOrigin, final Supplier<R> newResult) {
            this.newOrigin = newOrigin;
            this.newResult = newResult;
        }

        @SuppressWarnings("unchecked")
        private static <V> Mapper<V, V> direct() {
            return DIRECT;
        }

        public final <VO, VR> Builder<O, R> add(final Property<O, VO> origin,
                                                final Property<R, VR> target,
                                                final Mapper<VO, VR> mapper) {
            entries.add(new Entry<>(origin, target, mapper));
            return this;
        }

        public final <V> Builder<O, R> add(final Property<O, V> origin, final Property<R, V> target) {
            return add(origin, target, direct());
        }

        public final PropertyMapper<O, R> build() {
            return new PropertyMapper<>(this);
        }
    }

    private static final class Entry<O, VO, R, VR> {

        private final Property<O, VO> originalProperty;
        private final Property<R, VR> resultingProperty;
        private final Mapper<VO, VR> mapper;

        private Entry(final Property<O, VO> originalProperty,
                      final Property<R, VR> resultingProperty,
                      final Mapper<VO, VR> mapper) {
            this.originalProperty = originalProperty;
            this.resultingProperty = resultingProperty;
            this.mapper = mapper;
        }

        private void forward(final O origin, final R result) {
            this.resultingProperty.set(result, mapper.apply(this.originalProperty.get(origin)));
        }

        private void reverse(final O origin, final R result) {
            this.originalProperty.set(origin, mapper.reversal().apply(resultingProperty.get(result)));
        }
    }

    private class Reversal implements Mapper<R, O> {

        private final List<Entry<O, ?, R, ?>> entries;
        private final Supplier<O> newOrigin;

        private Reversal(final List<Entry<O, ?, R, ?>> entries, final Supplier<O> newOrigin) {
            this.entries = entries;
            this.newOrigin = newOrigin;
        }

        @Override
        public final O apply(final R origin) {
            final O result = newOrigin.get();
            for (final Entry<O, ?, R, ?> entry : entries) {
                entry.reverse(result, origin);
            }
            return result;
        }

        @Override
        public final Mapper<O, R> reversal() {
            return PropertyMapper.this;
        }
    }
}
