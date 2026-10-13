/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents an 8-color gradient palette sampled from an 8x1 image to colorize armor trims.
 * <p>
 * Armor trims are decomposed into 8 grayscale shading layers; this palette maps each layer
 * to a distinct color tint in client {@code custom_model_data} definitions.
 * <p>
 * Example:
 * <pre>{@code
 * BufferedImage img = ImageIO.read(paletteFile);
 * ArmorTrimPalette palette = ArmorTrimPalette.from(img);
 * List<ArmorColor> colors = palette.palettes();
 * }</pre>
 *
 * @param palettes unmodifiable list of exactly 8 {@link ArmorColor} values
 * @since 1.2.0
 */
public record ArmorTrimPalette(@NotNull @Unmodifiable List<ArmorColor> palettes) {

    /**
     * Extracts an 8-color trim palette from an 8x1 pixel image.
     *
     * @param image 8x1 pixel buffered image
     * @return parsed {@link ArmorTrimPalette}
     * @throws AssertionError if the image width is not 8 or height is not 1
     */
    public static @NotNull ArmorTrimPalette from(@NotNull BufferedImage image) {
        assert image.getHeight() == 1 && image.getWidth() == 8;
        return new ArmorTrimPalette(IntStream.range(0, 8)
            .mapToObj(i -> ArmorColor.from(image.getRGB(i, 0)))
            .toList());
    }
}
