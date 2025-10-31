package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public record ArmorPaletteImage(@NotNull String name, @NotNull BufferedImage image) {
}
