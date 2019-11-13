package de.team33.test.mapping.v9;

import de.team33.libs.mapping.v9.Codec;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

/**
 * Basic test for {@link Codec} implementations
 */
public abstract class CodecTestBase<O, R> {

    private final Codec<O, R> codec;
    private final int repetitions;

    protected CodecTestBase(final Codec<O, R> codec, final int repetitions) {
        this.codec = codec;
        this.repetitions = repetitions;
    }

    /**
     * Returns a random but applicable instance of the original type.
     */
    protected abstract O anyOrigin();

    /**
     * Returns a random but applicable instance of the resulting type.
     */
    protected abstract R anyResult();

    @Test
    public final void forward() {
        Stream.generate(this::anyOrigin).limit(repetitions).forEach(origin -> {
            final R stage = codec.apply(origin);
            final O result = codec.reversal().apply(stage);
            Assert.assertEquals(origin, result);
        });
    }

    @Test
    public final void reverse() {
        Stream.generate(this::anyResult).limit(repetitions).forEach(origin -> {
            final O stage = codec.reversal().apply(origin);
            final R result = codec.apply(stage);
            Assert.assertEquals(origin, result);
        });
    }
}
