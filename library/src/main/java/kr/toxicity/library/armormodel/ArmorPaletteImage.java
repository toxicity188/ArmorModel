/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Container pairing a palette identifier with an 8x1 pixel trim color palette image.
 * <p>
 * Supplies color data used to generate runtime {@link ArmorTrimPalette} instances for tinting
 * armor trim layers in client item models.
 * <p>
 * Example:
 * <pre>{@code
 * BufferedImage image = ImageIO.read(paletteFile);
 * ArmorPaletteImage palette = new ArmorPaletteImage("amethyst", image);
 * }</pre>
 *
 * @param name unique name of the palette (e.g. "amethyst")
 * @param image 8x1 pixel buffered image containing the trim color gradient
 * @since 1.2.0
 */
public record ArmorPaletteImage(@NotNull String name, @NotNull BufferedImage image) {
}
