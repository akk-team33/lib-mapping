package de.team33.libs.mapping.v6;

import java.util.function.Function;

public class PlainMapper<O, R> implements Mapper<O, R> {

    private final Function<O, R> forward;
    private final Mapper<R, O> reverse;

    public PlainMapper(final Function<O, R> forward, final Function<R, O> reverse) {
        this.forward = forward;
        this.reverse = new Reverse(reverse);
    }

    @Override
    public final R apply(final O origin) {
        return forward.apply(origin);
    }

    @Override
    public final Mapper<R, O> reversal() {
        return reverse;
    }

    private final class Reverse implements Mapper<R, O> {

        private final Function<R, O> method;

        private Reverse(final Function<R, O> method) {
            this.method = method;
        }

        @Override
        public final O apply(final R origin) {
            return method.apply(origin);
        }

        @Override
        public final Mapper<O, R> reversal() {
            return PlainMapper.this;
        }
    }
}
