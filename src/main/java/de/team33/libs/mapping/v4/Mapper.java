package de.team33.libs.mapping.v4;

@FunctionalInterface
public interface Mapper<S, R> {

    R map(S source);
}
