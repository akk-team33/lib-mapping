package de.team33.libs.mapping.v8;

import de.team33.libs.typing.v3.Type;

import java.util.function.Function;

public final class Normalizer {

    private Normalizer(final Builder builder) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final Normal encode(final Object origin) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final <T> T decode(final Normal normal, final Type<T> type) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder {

        public final <T> Builder addMethod(final Class<T> type,
                                           final Function<T, Normal> encoder,
                                           final Function<Normal, T> decoder) {
            return addMethod(Type.of(type), encoder, decoder);
        }

        public final <T> Builder addMethod(final Type<T> type,
                                           final Function<T, Normal> encoder,
                                           final Function<Normal, T> decoder) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public final Normalizer build() {
            return new Normalizer(this);
        }
    }
}
