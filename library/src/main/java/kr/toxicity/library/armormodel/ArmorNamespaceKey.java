/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a Minecraft namespaced resource identifier formatted as {@code namespace:itemPath/item}.
 * <p>
 * Used to identify client item models and texture assets within a resource pack.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorNamespaceKey key = new ArmorNamespaceKey("armormodel", "armor", "helmet");
 * String formatted = key.toString(); // "armormodel:armor/helmet"
 * }</pre>
 *
 * @param namespace the resource pack namespace (e.g. "armormodel")
 * @param itemPath relative directory path within the model or texture directory
 * @param item item or model file name without extension
 * @since 1.2.0
 */
public record ArmorNamespaceKey(@NotNull String namespace, @NotNull String itemPath, @NotNull String item) {
    /**
     * Formats this key into a standard Minecraft resource location string ({@code namespace:itemPath/item}).
     *
     * @return formatted resource location string
     */
    @Override
    public @NotNull String toString() {
        return namespace + ":" + itemPath + "/" + item;
    }
}
