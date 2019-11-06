package de.team33.libs.mapping.v5;

import java.util.function.Function;

public interface BiMapper<L, R> {

    static <L, R> BiMapper<L, R> composed(final Function<L, R> forward, final Function<R, L> revers) {
        return new ComposedMapper<>(forward, revers);
    }

    R toRight(L left);

    L toLeft(R right);

}
