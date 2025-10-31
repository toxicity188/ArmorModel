package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

public record ArmorTrimPalette(@NotNull @Unmodifiable List<ArmorColor> palettes) {

    public static @NotNull ArmorTrimPalette from(@NotNull BufferedImage image) {
        assert image.getHeight() == 1 && image.getWidth() == 8;
        return new ArmorTrimPalette(IntStream.range(0, 8)
                .mapToObj(i -> ArmorColor.from(image.getRGB(i, 0)))
                .toList());
    }
}
