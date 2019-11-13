package de.team33.libs.mapping.v9;

import java.util.function.Function;

/**
 * <p>A {@link Codec} is a {@link Function} that can map certain information from one format to another, ideally
 * without changing the information itself. In addition, a codec provides its own reversal. Different formats should
 * be represented by different data types.</p>
 *
 * <p>In practice, information losses during encoding can not always be completely avoided. An implementation should
 * document its loss potential.</p>
 *
 * @param <O> The original type
 * @param <R> The resulting type
 */
public interface Codec<O, R> extends Function<O, R> {

    /**
     * <p>Provides the reversal of this {@link Codec}.</p>
     */
    Codec<R, O> reversal();
}
