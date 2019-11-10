package de.team33.test.mapping.v6;

import de.team33.libs.mapping.v6.Mapper;
import org.junit.Assert;
import org.junit.Test;

public abstract class MapperTestBase<O, R> {

    private final Mapper<O, R> mapper;

    protected MapperTestBase(final Mapper<O, R> mapper) {
        this.mapper = mapper;
    }

    protected abstract O anyOrigin();

    protected abstract R anyResult();

    @Test
    public final void map() {
        final O origin = anyOrigin();
        final R stage = mapper.apply(origin);
        final O result = mapper.reversal().apply(stage);
        Assert.assertEquals(origin, result);
    }

    @Test
    public final void reverse() {
        final R origin = anyResult();
        final O stage = mapper.reversal().apply(origin);
        final R result = mapper.apply(stage);
        Assert.assertEquals(origin, result);
    }
}
