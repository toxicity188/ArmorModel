package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record ArmorNameMapper(
        @NotNull Function<String, String> textures,
        @NotNull Function<String, String> models
) {
    public static final ArmorNameMapper DEFAULT = new ArmorNameMapper(
            s -> s,
            s -> s
    );
}
