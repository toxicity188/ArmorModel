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

public record ArmorCustomModelData(
    @NotNull ArmorNamespaceKey itemModel,
    @NotNull List<String> strings,
    @NotNull IntList colors
) {
}
