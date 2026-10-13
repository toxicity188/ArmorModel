/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Enumeration of armor bones and body part models used in custom armor rendering.
 * <p>
 * Represents individual decomposed bone models (e.g. helmet, chest, waist, limbs)
 * rendered on entities via {@code item_display} or packet-based equipment slots.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorData helmetData = ArmorResource.HELMET.read(ArmorResource.StreamLoader.DEFAULT);
 * }</pre>
 *
 * @since 1.2.0
 */
@RequiredArgsConstructor
public enum ArmorResource {
    /**
     * Head armor piece model.
     */
    HELMET("armor/helmet.json"),
    /**
     * Upper torso chestplate model.
     */
    CHEST("armor/chest.json"),
    /**
     * Waist torso armor model.
     */
    WAIST("armor/waist.json"),
    /**
     * Hip armor model (upper leggings).
     */
    HIP("armor/hip.json"),
    /**
     * Left arm armor model.
     */
    LEFT_ARM("armor/left_arm.json"),
    /**
     * Right arm armor model.
     */
    RIGHT_ARM("armor/right_arm.json"),
    /**
     * Left upper leg armor model.
     */
    LEFT_LEG("armor/left_leg.json"),
    /**
     * Right upper leg armor model.
     */
    RIGHT_LEG("armor/right_leg.json"),
    /**
     * Left lower leg / boot armor model.
     */
    LEFT_FORELEG("armor/left_foreleg.json"),
    /**
     * Right lower leg / boot armor model.
     */
    RIGHT_FORELEG("armor/right_foreleg.json"),
    ;
    private final @NotNull String path;

    /**
     * Reads and parses this armor bone model template JSON using the specified stream loader.
     *
     * @param loader stream loader to retrieve the JSON resource stream
     * @return parsed {@link ArmorData} instance
     * @throws RuntimeException if an I/O error occurs while reading the template stream
     */
    public @NotNull ArmorData read(@NotNull StreamLoader loader) {
        try (
            var stream = loader.load(path);
            var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
        ) {
            return ArmorData.GSON.fromJson(reader, ArmorData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Functional interface for loading resource input streams from classpath or custom sources.
     * <p>
     * Example:
     * <pre>{@code
     * ArmorResource.StreamLoader loader = path -> plugin.getResource(path);
     * }</pre>
     *
     * @since 1.2.0
     */
    @FunctionalInterface
    public interface StreamLoader {
        /**
         * Default stream loader loading resources from the JAR classpath.
         */
        StreamLoader DEFAULT = path -> Objects.requireNonNull(ArmorResource.class.getResourceAsStream("/" + path));

        /**
         * Opens an input stream for the specified resource path.
         *
         * @param path relative path to the resource file
         * @return open {@link InputStream}
         * @throws IOException if an I/O error occurs while opening the stream
         */
        @NotNull InputStream load(@NotNull String path) throws IOException;
    }
}
