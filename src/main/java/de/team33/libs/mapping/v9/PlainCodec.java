package de.team33.libs.mapping.v9;

import java.util.function.Function;

public class PlainCodec<O, R> implements Codec<O, R> {

    private final Function<O, R> forward;
    private final Codec<R, O> reverse;

    public PlainCodec(final Function<O, R> forward, final Function<R, O> reverse) {
        this.forward = forward;
        this.reverse = new Reversal(reverse);
    }

    @Override
    public final R apply(final O origin) {
        return forward.apply(origin);
    }

    @Override
    public final Codec<R, O> reversal() {
        return reverse;
    }

    private final class Reversal implements Codec<R, O> {

        private final Function<R, O> method;

        private Reversal(final Function<R, O> method) {
            this.method = method;
        }

        @Override
        public final O apply(final R origin) {
            return method.apply(origin);
        }

        @Override
        public final Codec<O, R> reversal() {
            return PlainCodec.this;
        }
    }
}
