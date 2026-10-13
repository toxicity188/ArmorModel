/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public record ArmorImage(@NotNull String name, @NotNull BufferedImage armor, @NotNull BufferedImage leggings) {
    public @NotNull String armorName() {
        return name + "_armor";
    }
    public @NotNull String leggingsName() {
        return name + "_armor_leggings";
    }
}
