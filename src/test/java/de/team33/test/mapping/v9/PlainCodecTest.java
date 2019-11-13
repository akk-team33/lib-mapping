package de.team33.test.mapping.v9;

import de.team33.libs.mapping.v9.PlainCodec;

import java.util.Random;

public class PlainCodecTest extends CodecTestBase<Long, String> {

    private final Random random = new Random();

    public PlainCodecTest() {
        super(new PlainCodec<>(String::valueOf, Long::valueOf), 100);
    }

    @Override
    protected final Long anyOrigin() {
        return random.nextLong();
    }

    @Override
    protected final String anyResult() {
        return anyOrigin().toString();
    }
}