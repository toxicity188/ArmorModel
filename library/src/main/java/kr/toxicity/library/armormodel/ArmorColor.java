/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a 32-bit ARGB color for armor tinting and palette processing.
 * <p>
 * Used in packet-based armor rendering and item display models to sample and apply
 * RGB color layers to item models.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorColor color = ArmorColor.from(0xFF336699);
 * int rgb = color.rgb();
 * int average = color.avg();
 * }</pre>
 *
 * @param a alpha component (0-255)
 * @param r red component (0-255)
 * @param g green component (0-255)
 * @param b blue component (0-255)
 * @since 1.2.0
 */
public record ArmorColor(int a, int r, int g, int b) {

    static final ArmorColor WHITE = new ArmorColor(0xFF, 0xFF, 0xFF, 0xFF);

    /**
     * Unpacks a 32-bit ARGB integer into an {@link ArmorColor}.
     *
     * @param value packed 32-bit ARGB integer
     * @return color representation
     */
    public static @NotNull ArmorColor from(int value) {
        return new ArmorColor(
            (value >> 24) & 0xFF,
            (value >> 16) & 0xFF,
            (value >> 8) & 0xFF,
            value & 0xFF
        );
    }

    /**
     * Calculates the arithmetic mean of red, green, and blue channels.
     *
     * @return average RGB channel intensity (0-255)
     */
    public int avg() {
        return (r + g + b) / 3;
    }

    /**
     * Packs this color into a 32-bit ARGB integer.
     *
     * @return packed 32-bit ARGB integer
     */
    public int value() {
        return (a << 24) | rgb();
    }

    /**
     * Packs the red, green, and blue channels into a 24-bit RGB integer.
     *
     * @return packed 24-bit RGB integer
     */
    public int rgb() {
        return (r << 16) | (g << 8) | b;
    }
}
