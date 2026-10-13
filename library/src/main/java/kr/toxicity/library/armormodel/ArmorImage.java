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
 * Paired texture container holding standard layer 1 armor and layer 2 leggings textures.
 * <p>
 * Supplies texture inputs for generating client-side models and item display renderings.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorImage image = new ArmorImage("copper", armorImg, leggingsImg);
 * String armorName = image.armorName(); // "copper_armor"
 * String leggingsName = image.leggingsName(); // "copper_armor_leggings"
 * }</pre>
 *
 * @param name identifier for the armor texture (e.g. "copper")
 * @param armor layer 1 texture image (helmets, chestplates, boots)
 * @param leggings layer 2 texture image (leggings)
 * @since 1.2.0
 */
public record ArmorImage(@NotNull String name, @NotNull BufferedImage armor, @NotNull BufferedImage leggings) {
    /**
     * Returns the formatted texture name for layer 1 armor.
     *
     * @return layer 1 texture name
     */
    public @NotNull String armorName() {
        return name + "_armor";
    }

    /**
     * Returns the formatted texture name for layer 2 leggings.
     *
     * @return layer 2 leggings texture name
     */
    public @NotNull String leggingsName() {
        return name + "_armor_leggings";
    }
}
