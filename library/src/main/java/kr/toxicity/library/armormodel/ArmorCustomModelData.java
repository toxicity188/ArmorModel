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
