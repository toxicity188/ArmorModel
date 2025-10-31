package kr.toxicity.library.armormodel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

final class ArmorNamespace {

    private final ArmorModelContext context;
    final String namespace;

    final Builder items, textures, models;

    ArmorNamespace(
            @NotNull ArmorModelContext context,
            @NotNull String namespace
    ) {
        this.context = context;
        this.namespace = namespace;

        items = new Builder("assets/" + namespace + "/items/armor/");
        textures = new Builder("assets/" + namespace + "/textures/item/armor/");
        models = new Builder("assets/" + namespace + "/models/armor/");
    }

    @RequiredArgsConstructor
    final class Builder {
        private final String prefix;

        <T> void add(@NotNull String name, @NotNull T value, @NotNull Function<T, byte[]> function) {
            context.builders.add(ArmorByteBuilder.ofOnce(prefix + name, value, function));
        }

        void add(@NotNull String name, @NotNull ArmorData data) {
            add(name, data, d -> d.toJson().getBytes(StandardCharsets.UTF_8));
        }

        void add(@NotNull String name, @NotNull BufferedImage image) {
            add(name, image, img -> {
                try (
                        var buffer = new ByteArrayOutputStream()
                ) {
                    ImageIO.write(img, "png", buffer);
                    return buffer.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
