package de.team33.test.mapping.shared;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings({"JUnitTestMethodWithNoAssertions", "UseOfSystemOutOrSystemErr"})
public class RandomPoolTest {

    private Pool<Random> pool = new Pool<>(Random::new);

    @Test
    public final void anyTimeNewRandom() throws InterruptedException {
        final List<Integer> results = Collections.synchronizedList(new ArrayList<>());
        System.out.println(test(() -> {
            final Random random = new Random();
            results.add(random.nextInt());
        }, 1, 1000000));
    }

    @Test
    public final void anyTimeSameRandom() throws InterruptedException {
        final List<Integer> results = Collections.synchronizedList(new ArrayList<>());
        final Random random = new Random();
        System.out.println(test(() -> {
            results.add(random.nextInt());
        }, 1, 1000000));
    }

    @Test
    public final void unpooledSingleThreaded() throws InterruptedException {
        System.out.println(test(Random::new, 1, 10000));
    }

    @Test
    public final void unpooledMultiThreaded() throws InterruptedException {
        System.out.println(test(Random::new, 100, 1000));
    }

    @Test
    public final void pooledSingleThreaded() throws InterruptedException {
        System.out.println(test(() -> pool.run(r -> {}), 1, 10000));
    }

    @Test
    public final void pooledMultiThreaded() throws InterruptedException {
        System.out.println(test(() -> pool.run(r -> {}), 100, 1000));
    }

    private static long test(final Runnable operation, final int threadCount, final int instancesPerThread) throws InterruptedException {
        final Thread[] threads = newThreads(operation, threadCount, instancesPerThread);
        final long time0 = System.currentTimeMillis();
        start(threads);
        join(threads);
        return System.currentTimeMillis() - time0;
    }

    private static Thread[] newThreads(final Runnable operation, final int threadCount, final int instancesPerThread) {
        final Thread[] result = new Thread[threadCount];
        for (int i = 0; i < threadCount; ++i) {
            result[i] = newThread(operation, instancesPerThread);
        }
        return result;
    }

    private static Thread newThread(final Runnable operation, final int instances) {
        return new Thread(() -> {
            for (int k = 0; k < instances; ++k) {
                operation.run();
            }
        });
    }

    private static void join(final Thread[] threads) throws InterruptedException {
        for (final Thread thread : threads) {
            thread.join();
        }
    }

    private static void start(final Thread[] threads) {
        for (final Thread thread : threads) {
            thread.start();
        }
    }
}
