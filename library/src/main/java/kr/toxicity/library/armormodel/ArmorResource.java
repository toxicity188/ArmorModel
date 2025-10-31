package kr.toxicity.library.armormodel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RequiredArgsConstructor
public enum ArmorResource {
    HELMET("armor/helmet.json"),
    CHEST("armor/chest.json"),
    WAIST("armor/waist.json"),
    HIP("armor/hip.json"),
    LEFT_ARM("armor/left_arm.json"),
    RIGHT_ARM("armor/right_arm.json"),
    LEFT_LEG("armor/left_leg.json"),
    RIGHT_LEG("armor/right_leg.json"),
    LEFT_FORELEG("armor/left_foreleg.json"),
    RIGHT_FORELEG("armor/right_foreleg.json"),
    ;
    private final @NotNull String path;

    public @NotNull String path() {
        return path;
    }

    public @NotNull ArmorData read(@NotNull StreamLoader loader) {
        try (
                var stream = loader.load(path);
                var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
        ) {
            return ArmorData.GSON.fromJson(reader, ArmorData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface StreamLoader {
        StreamLoader DEFAULT = path -> Objects.requireNonNull(ArmorResource.class.getResourceAsStream("/" + path));

        @NotNull InputStream load(@NotNull String path) throws IOException;
    }
}
