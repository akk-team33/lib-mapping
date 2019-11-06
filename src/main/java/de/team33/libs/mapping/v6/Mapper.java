package de.team33.libs.mapping.v6;

public interface Mapper<O, R> {

    R map(O origin);

    Mapper<R, O> reverse();
}
