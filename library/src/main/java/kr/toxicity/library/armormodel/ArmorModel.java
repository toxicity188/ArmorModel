/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Root container and compiler for custom Minecraft armor models, trims, and client assets.
 * <p>
 * Converts armor and trim textures into decomposed bone models and JSON definitions compatible with
 * Minecraft 1.21.4+ item models, supporting packet-based equipment and {@code item_display} entity rendering.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorModel armorModel = ArmorModel.builder()
 *     .namespace("myplugin")
 *     .armors(List.of(new ArmorImage("copper", copperArmor, copperLeggings)))
 *     .armorTrims(List.of(new ArmorImage("bolt", boltArmor, boltLeggings)))
 *     .palettes(List.of(new ArmorPaletteImage("amethyst", amethystImg)))
 *     .build();
 *
 * ArmorDataResult chestResult = armorModel.resource(ArmorResource.CHEST);
 * }</pre>
 *
 * @param builders unmodifiable list of generated resource pack asset builders
 * @param resources mapping of armor bones to compiled item model definitions
 * @param colors mapping of palette names to parsed 8-step trim palettes
 * @since 1.2.0
 */
public record ArmorModel(
    @NotNull @Unmodifiable List<ArmorByteBuilder> builders,
    @NotNull @Unmodifiable Map<ArmorResource, ArmorDataResult> resources,
    @NotNull @Unmodifiable Map<String, ArmorTrimPalette> colors
) {

    /**
     * Empty {@link ArmorModel} instance containing no builders, resources, or palettes.
     */
    public static final ArmorModel EMPTY = new ArmorModel(
        Collections.emptyList(),
        Collections.emptyMap(),
        Collections.emptyMap()
    );

    /**
     * Retrieves the compiled model data result for the specified armor bone.
     *
     * @param resource armor bone resource to look up
     * @return compiled model data result
     * @throws NullPointerException if the resource is not found in the model map
     */
    public @NotNull ArmorDataResult resource(@NotNull ArmorResource resource) {
        return Objects.requireNonNull(resources.get(resource));
    }

    /**
     * Creates a new fluent {@link Builder} to configure and compile an {@link ArmorModel}.
     *
     * @return a new model builder instance
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder for compiling {@link ArmorModel} assets and client definitions.
     * <p>
     * Example:
     * <pre>{@code
     * ArmorModel model = ArmorModel.builder()
     *     .namespace("myplugin")
     *     .itemPath("armor")
     *     .armors(armorList)
     *     .build();
     * }</pre>
     *
     * @since 1.2.0
     */
    public static final class Builder {
        private ArmorResource.StreamLoader streamLoader = ArmorResource.StreamLoader.DEFAULT;
        private List<ArmorImage> armors = Collections.emptyList();
        private List<ArmorImage> armorTrims = Collections.emptyList();
        private List<ArmorPaletteImage> palettes = Collections.emptyList();
        private String namespace = "armormodel";
        private String itemPath = "armor";
        private ArmorNameMapper nameMapper = ArmorNameMapper.DEFAULT;
        private boolean flush = true;

        private Builder() {
        }

        /**
         * Sets the stream loader used to read baseline bone JSON templates from the classpath or filesystem.
         *
         * @param streamLoader template resource stream loader
         * @return this builder
         * @throws NullPointerException if streamLoader is null
         */
        public @NotNull Builder streamLoader(@NotNull ArmorResource.StreamLoader streamLoader) {
            this.streamLoader = Objects.requireNonNull(streamLoader);
            return this;
        }

        /**
         * Sets the list of base armor textures to process.
         *
         * @param armors list of base armor images
         * @return this builder
         * @throws NullPointerException if armors is null
         */
        public @NotNull Builder armors(@NotNull List<ArmorImage> armors) {
            this.armors = Objects.requireNonNull(armors);
            return this;
        }

        /**
         * Sets the list of armor trim overlay textures to process.
         *
         * @param armors list of armor trim images
         * @return this builder
         * @throws NullPointerException if armors is null
         */
        public @NotNull Builder armorTrims(@NotNull List<ArmorImage> armors) {
            this.armorTrims = Objects.requireNonNull(armors);
            return this;
        }

        /**
         * Sets the list of 8x1 trim palette gradient textures to register.
         *
         * @param armors list of palette images
         * @return this builder
         * @throws NullPointerException if armors is null
         */
        public @NotNull Builder palettes(@NotNull List<ArmorPaletteImage> armors) {
            this.palettes = Objects.requireNonNull(armors);
            return this;
        }

        /**
         * Sets the target resource pack namespace (e.g. "minecraft" or "myplugin").
         *
         * @param namespace target namespace
         * @return this builder
         * @throws NullPointerException if namespace is null
         */
        public @NotNull Builder namespace(@NotNull String namespace) {
            this.namespace = Objects.requireNonNull(namespace);
            return this;
        }

        /**
         * Sets the sub-directory path used for item models and textures within the namespace.
         *
         * @param itemPath item subfolder path
         * @return this builder
         * @throws NullPointerException if itemPath is null
         */
        public @NotNull Builder itemPath(@NotNull String itemPath) {
            this.itemPath = Objects.requireNonNull(itemPath);
            return this;
        }

        /**
         * Sets the asset naming mapper for rewriting texture and model paths.
         *
         * @param nameMapper path transformation mapper
         * @return this builder
         * @throws NullPointerException if nameMapper is null
         */
        public @NotNull Builder nameMapper(@NotNull ArmorNameMapper nameMapper) {
            this.nameMapper = Objects.requireNonNull(nameMapper);
            return this;
        }

        /**
         * Configures whether to automatically register item definition JSON builders during build.
         *
         * @param flush true to generate item definition files, false to omit
         * @return this builder
         */
        public @NotNull Builder flush(boolean flush) {
            this.flush = flush;
            return this;
        }

        /**
         * Compiles the configured textures, trims, and palettes into a complete {@link ArmorModel}.
         *
         * @return compiled {@link ArmorModel} instance
         */
        public @NotNull ArmorModel build() {
            try (var context = new ArmorModelContext(
                Arrays.stream(ArmorResource.values())
                    .map(value -> new ArmorDataContext(value, streamLoader))
                    .toList(),
                namespace,
                itemPath,
                nameMapper,
                flush
            )) {
                context.loadArmor(armors);
                context.loadArmorTrim(armorTrims);
                return new ArmorModel(
                    Collections.unmodifiableList(context.builders),
                    context.results(),
                    palettes.stream().collect(Collectors.toUnmodifiableMap(
                        ArmorPaletteImage::name,
                        p -> ArmorTrimPalette.from(p.image())
                    ))
                );
            }
        }
    }
}
