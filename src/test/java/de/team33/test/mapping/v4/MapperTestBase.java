package de.team33.test.mapping.v4;

import de.team33.libs.mapping.v4.Mapper;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class MapperTestBase<S, R> {

    protected abstract S newSource();

    protected abstract R newResult();

    protected abstract Mapper<S, R> mapper();

    protected abstract Mapper<R, S> remapper();

    @Test
    public final void map() {
        final S source = newSource();
        final R result = mapper().map(source);
        assertEquals(source, remapper().map(result));
    }

    @Test
    public final void remap() {
        final R source = newResult();
        final S result = remapper().map(source);
        assertEquals(source, mapper().map(result));
    }
}
