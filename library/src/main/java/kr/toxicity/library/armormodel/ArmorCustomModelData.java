/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Contains Minecraft 1.21.4+ client model metadata applied to CustomModelData components.
 * <p>
 * Provides string tokens for select-based model matching and RGB tint colors for composite layers,
 * enabling dynamic armor model and trim rendering via packet-based equipment or {@code item_display} entities.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorCustomModelData cmd = dataResult.customModelData("copper", "bolt", palette);
 * itemMeta.setItemModel(NamespacedKey.fromString(cmd.itemModel().toString()));
 * var component = itemMeta.getCustomModelDataComponent();
 * component.setStrings(cmd.strings());
 * component.setColors(cmd.colors().intStream().mapToObj(Color::fromRGB).toList());
 * itemMeta.setCustomModelDataComponent(component);
 * }</pre>
 *
 * @param itemModel target item model resource key
 * @param strings string tokens matched by client {@code select} model definitions
 * @param colors tint color integers passed to {@code custom_model_data} tints
 * @since 1.2.0
 */
public record ArmorCustomModelData(
    @NotNull ArmorNamespaceKey itemModel,
    @NotNull List<String> strings,
    @NotNull IntList colors
) {
}
