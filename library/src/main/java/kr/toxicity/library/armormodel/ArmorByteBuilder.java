package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ArmorByteBuilder extends Supplier<byte[]> {
    @NotNull String path();

    static <T> @NotNull ArmorByteBuilder ofOnce(@NotNull String path, @NotNull T value, @NotNull Function<T, byte[]> function) {
        return new ArmorByteBuilder() {

            private final AtomicReference<T> ref = new AtomicReference<>(value);

            @Override
            public @NotNull String path() {
                return path;
            }

            @Override
            public byte[] get() {
                var get = ref.getAndSet(null);
                if (get == null) throw new UnsupportedOperationException();
                return function.apply(get);
            }
        };
    }
}
