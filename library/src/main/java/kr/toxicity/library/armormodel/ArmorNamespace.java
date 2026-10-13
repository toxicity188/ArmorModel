/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Internal resource pack directory path resolver and asset registrant for a specific namespace.
 */
final class ArmorNamespace {

    private final ArmorModelContext context;
    final String namespace;
    final String itemPath;

    final Builder items, textures, models;

    ArmorNamespace(
        @NotNull ArmorModelContext context,
        @NotNull String namespace,
        @NotNull String itemPath
    ) {
        this.context = context;
        this.namespace = namespace;
        this.itemPath = itemPath;

        items = new Builder("assets/" + namespace + "/items/" + itemPath + "/");
        textures = new Builder("assets/" + namespace + "/textures/item/" + itemPath + "/");
        models = new Builder("assets/" + namespace + "/models/" + itemPath + "/");
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
