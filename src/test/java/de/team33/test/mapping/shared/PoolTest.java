package de.team33.test.mapping.shared;

import org.junit.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class PoolTest {

    private final Pool<Random> randomPool = new Pool<>(Random::new);

    @Test
    public final void run() {
        try {
            randomPool.run(PoolTest::throwingIOException);
        } catch (final IOException e) {
            // ignorable;
        }
    }

    @Test
    public final void get() {
        try {
            randomPool.get(PoolTest::throwingIOException);
        } catch (final IOException e) {
            // ignorable;
        }
    }

    private static int throwingIOException(final Random random) throws IOException {
        return Optional.of(random.nextInt())
                       .filter(i -> 0 != i)
                       .orElseThrow(IOException::new);
    }
}
