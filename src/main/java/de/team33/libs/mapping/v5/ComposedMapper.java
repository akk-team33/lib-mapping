package de.team33.libs.mapping.v5;

import java.util.function.Function;

public class ComposedMapper<L, R> implements BiMapper<L, R> {

    private final Function<L, R> toRight;
    private final Function<R, L> toLeft;

    public ComposedMapper(final Function<L, R> toRight, final Function<R, L> toLeft) {
        this.toRight = toRight;
        this.toLeft = toLeft;
    }

    @Override
    public final R toRight(final L left) {
        return toRight.apply(left);
    }

    @Override
    public final L toLeft(final R right) {
        return toLeft.apply(right);
    }
}
