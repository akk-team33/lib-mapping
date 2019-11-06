package de.team33.test.mapping.v5;

import de.team33.libs.mapping.v5.BiMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class BiMapperTestBase<L, R> {

    protected abstract BiMapper<L, R> mapper();

    protected abstract L newLeft();

    protected abstract R newRight();

    @Test
    public void toRight() {
        final L origin = newLeft();
        final R stage = mapper().toRight(origin);
        final L result = mapper().toLeft(stage);
        assertEquals(origin, result);
    }

    @Test
    public void toLeft() {
        final R origin = newRight();
        final L stage = mapper().toLeft(origin);
        final R result = mapper().toRight(stage);
        assertEquals(origin, result);
    }
}
