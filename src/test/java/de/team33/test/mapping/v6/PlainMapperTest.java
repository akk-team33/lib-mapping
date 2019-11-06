package de.team33.test.mapping.v6;

import de.team33.libs.mapping.v6.PlainMapper;

import java.util.Random;

public class PlainMapperTest extends MapperTestBase<Long, String>{

    private final Random random = new Random();

    public PlainMapperTest() {
        super(new PlainMapper<>(Object::toString, Long::valueOf));
    }

    @Override
    protected final Long newOrigin() {
        return random.nextLong();
    }

    @Override
    protected final String newResult() {
        return newOrigin().toString();
    }
}
