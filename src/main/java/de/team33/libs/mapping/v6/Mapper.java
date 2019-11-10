package de.team33.libs.mapping.v6;

import java.util.function.Function;

/**
 * A {@link Mapper}, as defined here, is primarily a {@link Function} that can also be its inverse function.
 * Its primary task is to map information of one type to another, ideally without changing the information itself.
 *
 * @param <O> The original type of data to be mapped.
 * @param <R> The result type of the mapped data.
 */
public interface Mapper<O, R> extends Function<O, R> {

    R apply(O origin);

    Mapper<R, O> reversal();
}
