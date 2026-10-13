/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public record ArmorPaletteImage(@NotNull String name, @NotNull BufferedImage image) {
}
