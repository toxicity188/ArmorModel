package kr.toxicity.library.armormodel;

import com.google.gson.JsonArray;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

import static kr.toxicity.library.armormodel.ArmorUtil.*;

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
                context.namespace.textures.add(context.textureName(armorName + "_" + i) + ".png", armorSplit.get(i));
                context.namespace.textures.add(context.textureName(leggingsName + "_" + i) + ".png", leggingsSplit.get(i));
            }
            for (var datum : context.data) {
                var modelName = image.name() + "_" + datum.resource.name().toLowerCase();
                var array = new JsonArray();
                for (var i : TRIM_PALETTE_ARRAY) {
                    var subModelName = modelName + "_" + i;
                    context.namespace.models.add(
                            context.modelName(subModelName) + ".json",
                            datum.data.ofTextures(
                                    context.textureNamespace(armorName + "_" + i),
                                    context.textureNamespace(leggingsName + "_" + i)
                            )
                    );
                    array.add(modelJson(context.modelNamespace(subModelName), i));
                }
                datum.armorTrims.add(whenJson(
                        image.name(),
                        compositeJson(array)
                ));
            }
        }

        private static @NotNull Int2ObjectMap<BufferedImage> splitTrimImage(@NotNull BufferedImage image) {
            var width = image.getWidth();
            var height = image.getHeight();
            var map = new Int2ObjectAVLTreeMap<BufferedImage>();
            for (int i : TRIM_PALETTE_ARRAY) {
                map.put(i, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
            }
            var white = ArmorColor.WHITE.value();
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    var color = ArmorColor.from(image.getRGB(w, h));
                    if (color.a() < 0xFF) continue;
                    assert color.r() % 32 == 0 && color.g() % 32 == 0 && color.b() % 32 == 0;
                    map.get(8 - color.avg() / 32).setRGB(w, h, white);
                }
            }
            return map;
        }
    }
    ;
    abstract void process(@NotNull ArmorImage image, @NotNull ArmorModelContext context);
}
