/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lazy byte array builder for a generated resource pack asset.
 * <p>
 * Associates a relative file destination path with a byte payload generator.
 * Used to stream textures, models, and item definitions into client resource packs.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorByteBuilder builder = ArmorByteBuilder.ofOnce("assets/example/models/item/helmet.json", data, d -> {
 *     return d.toJson().getBytes(StandardCharsets.UTF_8);
 * });
 * String relativePath = builder.path();
 * byte[] data = builder.get();
 * }</pre>
 *
 * @since 1.2.0
 */
public interface ArmorByteBuilder extends Supplier<byte[]> {
    /**
     * Returns the relative file path within the resource pack.
     *
     * @return the relative file path
     */
    @NotNull String path();

    /**
     * Generates and returns the binary data for this resource.
     *
     * @return the binary payload
     * @throws UnsupportedOperationException if this builder is single-use and has already been evaluated
     */
    @Override
    byte[] get();

    /**
     * Creates a single-use byte builder that discards its reference after being consumed.
     *
     * @param <T> the intermediate value type
     * @param path the relative resource pack file path
     * @param value the data payload to convert
     * @param function mapping function to transform value into a byte array
     * @return a single-use {@link ArmorByteBuilder}
     * @throws NullPointerException if any argument is null
     */
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
