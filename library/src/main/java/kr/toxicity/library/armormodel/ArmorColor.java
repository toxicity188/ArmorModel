package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

public record ArmorColor(int a, int r, int g, int b) {

    static final ArmorColor WHITE = new ArmorColor(0xFF, 0xFF, 0xFF, 0xFF);

    public static @NotNull ArmorColor from(int value) {
        return new ArmorColor(
                (value >> 24) & 0xFF,
                (value >> 16) & 0xFF,
                (value >> 8) & 0xFF,
                value & 0xFF
        );
    }

    public int avg() {
        return (r + g + b) / 3;
    }

    public int value() {
        return (a << 24) | rgb();
    }

    public int rgb() {
        return (r << 16) | (g << 8) | b;
    }
}
