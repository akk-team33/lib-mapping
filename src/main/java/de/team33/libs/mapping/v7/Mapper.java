package de.team33.libs.mapping.v7;

/**
 * A {@link Mapper} provides a method to map information of one type to another,
 * ideally without changing the information itself.
 *
 * @param <O> the original type
 * @param <T> the target type
 */
public interface Mapper<O, T> {

    /**
     * Maps an information from an origin to a target and returns the target.
     */
    T map(O origin, T target);

    /**
     * Provides a  that provides a revers mapping.
     */
    Mapper<T, O> reversal();
}
