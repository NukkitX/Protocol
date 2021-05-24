package com.nukkitx.protocol.util;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface TriConsumer<T, U, R> {


    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param r the third input argument
     */
    void accept(T t, U u, R r);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<T, U, R> andThen(TriConsumer<? super T, ? super U, ? super R> after) {
        Objects.requireNonNull(after);

        return (l, m, r) -> {
            accept(l, m, r);
            after.accept(l, m, r);
        };
    }

    static <T, U, R> TriConsumer<T, U, R> from(BiConsumer<? super T, ? super R> consumer) {
        Objects.requireNonNull(consumer);
        return (l, m, r) -> consumer.accept(l, r);
    }
}
