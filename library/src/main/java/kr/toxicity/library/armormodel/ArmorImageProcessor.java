/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import com.google.gson.JsonArray;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

import static kr.toxicity.library.armormodel.ArmorUtil.*;

/**
 * Internal processor handling texture slicing, model generation, and trim luminance splitting.
 */
enum ArmorImageProcessor {
    ARMOR {
        @Override
        public void process(@NotNull ArmorImage image, @NotNull ArmorModelContext context) {
            var armorName = image.armorName();
            var leggingsName = image.leggingsName();
            context.namespace.textures.add(context.textureName(armorName) + ".png", image.armor());
            context.namespace.textures.add(context.textureName(leggingsName) + ".png", image.leggings());
            for (var datum : context.data) {
                var modelName = image.name() + "_" + datum.resource.name().toLowerCase();
                context.namespace.models.add(
                        context.modelName(modelName) + ".json",
                        datum.data.ofTextures(
                                context.textureNamespace(armorName),
                                context.textureNamespace(leggingsName)
                        )
                );
                datum.armors.add(whenJson(
                        image.name(),
                        modelJson(context.modelNamespace(modelName), 0)
                ));
            }
        }
    },
    ARMOR_TRIM {

        private static final int[] TRIM_PALETTE_ARRAY = { 1, 2, 3, 4, 5, 6, 7, 8 };

        @Override
        void process(@NotNull ArmorImage image, @NotNull ArmorModelContext context) {
            var armorName = image.armorName();
            var leggingsName = image.leggingsName();
            var armorSplit = splitTrimImage(image.armor());
            var leggingsSplit = splitTrimImage(image.leggings());
            for (var i : TRIM_PALETTE_ARRAY) {
                BufferedImage getImage;
                if ((getImage = armorSplit.get(i)) != null) context.namespace.textures.add(context.textureName(armorName + "_" + i) + ".png", getImage);
                if ((getImage = leggingsSplit.get(i)) != null) context.namespace.textures.add(context.textureName(leggingsName + "_" + i) + ".png", getImage);
            }
            for (var datum : context.data) {
                var modelName = image.name() + "_" + datum.resource.name().toLowerCase();
                var array = new JsonArray(TRIM_PALETTE_ARRAY.length);
                for (var i : TRIM_PALETTE_ARRAY) {
                    String armor, armorTrim;
                    armor = armorSplit.containsKey(i) ? context.textureNamespace(armorName + "_" + i) : null;
                    armorTrim = leggingsSplit.containsKey(i) ? context.textureNamespace(leggingsName + "_" + i) : null;
                    if (armor == null && armorTrim == null) continue;
                    var subModelName = modelName + "_" + i;
                    context.namespace.models.add(
                            context.modelName(subModelName) + ".json",
                            datum.data.ofTextures(armor, armorTrim)
                    );
                    array.add(modelJson(context.modelNamespace(subModelName), i));
                }
                if (array.isEmpty()) continue;
                datum.armorTrims.add(whenJson(
                        image.name(),
                        compositeJson(array)
                ));
            }
        }

        private static @NotNull Int2ObjectMap<BufferedImage> splitTrimImage(@NotNull BufferedImage image) {
            var width = image.getWidth();
            var height = image.getHeight();
            var map = new Int2ObjectOpenHashMap<DirtyImage>(TRIM_PALETTE_ARRAY.length);
            for (int i : TRIM_PALETTE_ARRAY) {
                map.put(i, new DirtyImage(width, height));
            }
            var white = ArmorColor.WHITE.value();
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    var color = ArmorColor.from(image.getRGB(w, h));
                    if (color.a() < 0xFF) continue;
                    map.get(8 - color.avg() / 32).set(w, h, white);
                }
            }
            var newMap = new Int2ObjectOpenHashMap<BufferedImage>(TRIM_PALETTE_ARRAY.length);
            var iterator = map.int2ObjectEntrySet().fastIterator();
            while (iterator.hasNext()) {
                var next = iterator.next();
                if (next.getValue().dirty()) newMap.put(next.getIntKey(), next.getValue().image);
            }
            return newMap;
        }

        private static final class DirtyImage {

            private final BufferedImage image;
            private boolean dirty = false;

            DirtyImage(int width, int height) {
                image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }

            void set(int width, int height, int color) {
                dirty = true;
                image.setRGB(width, height, color);
            }

            boolean dirty() {
                return dirty;
            }
        }
    }
    ;
    abstract void process(@NotNull ArmorImage image, @NotNull ArmorModelContext context);
}
