package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

public record ArmorNamespaceKey(@NotNull String namespace, @NotNull String item) {
    @Override
    public @NotNull String toString() {
        return namespace + ":" + item;
    }
}
