package de.team33.test.mapping.v3;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v3.Mapper;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

public abstract class MapperTestBase<T> {

    protected abstract Mapper<T> mapper();

    protected abstract T newSubject();

    protected abstract Map<String, Object> newMap();

    @Test
    public final void map() {
        final T origin = newSubject();
        final T target = newSubject();
        assertNotEquals(origin, target);

        final Map<String, Object> stage = mapper().map(origin);
        final T result = mapper().remap(stage, target);
        assertSame(target, result);
        assertEquals(origin, result);
    }

    @Test
    public final void remap() {
        final Map<String, Object> origin = newMap();
        final T stage = mapper().remap(origin, newSubject());
        final Map<String, Object> result = mapper().map(stage);
        assertEquals(origin, result);
    }
}
