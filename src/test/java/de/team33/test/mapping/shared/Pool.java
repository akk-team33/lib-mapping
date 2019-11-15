package de.team33.test.mapping.shared;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Pool<T> {

    private static final Void NULL = null;

    private final Supplier<T> newItem;
    private final Queue<T> stock = new ConcurrentLinkedQueue<>();

    public Pool(final Supplier<T> newItem) {
        this.newItem = newItem;
    }

    public final <X extends Exception> void run(final XConsumer<T, X> consumer) throws X {
        get(item -> {
            consumer.accept(item);
            return NULL;
        });
    }

    public final <R, X extends Exception> R get(final XFunction<T, R, X> function) throws X {
        final T item = Optional.ofNullable(stock.poll())
                               .orElseGet(newItem);
        try {
            return function.apply(item);
        } finally {
            stock.add(item);
        }
    }

    @FunctionalInterface
    public interface XConsumer<T, X extends Exception> {

        static <T> XConsumer<T, RuntimeException> of(final Consumer<T> consumer) {
            return consumer::accept;
        }

        void accept(T item) throws X;
    }

    @FunctionalInterface
    public interface XFunction<T, R, X extends Exception> {

        static <T, R> XFunction<T, R, RuntimeException> of(final Function<T, R> function) {
            return function::apply;
        }

        R apply(T item) throws X;
    }
}
