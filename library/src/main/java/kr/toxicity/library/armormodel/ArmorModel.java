package kr.toxicity.library.armormodel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public record ArmorModel(
        @NotNull @Unmodifiable List<ArmorByteBuilder> builders,
        @NotNull @Unmodifiable Map<ArmorResource, ArmorDataResult> resources,
        @NotNull @Unmodifiable Map<String, ArmorTrimPalette> colors
) {

    public static final ArmorModel EMPTY = new ArmorModel(
            Collections.emptyList(),
            Collections.emptyMap(),
            Collections.emptyMap()
    );

    public @NotNull ArmorDataResult resource(@NotNull ArmorResource resource) {
        return Objects.requireNonNull(resources.get(resource));
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {
        private ArmorResource.StreamLoader streamLoader = ArmorResource.StreamLoader.DEFAULT;
        private List<ArmorImage> armors = Collections.emptyList();
        private List<ArmorImage> armorTrims = Collections.emptyList();
        private List<ArmorPaletteImage> palettes = Collections.emptyList();
        private String namespace = "armormodel";
        private ArmorNameMapper nameMapper = ArmorNameMapper.DEFAULT;
        private boolean flush = true;

        public @NotNull Builder streamLoader(@NotNull ArmorResource.StreamLoader streamLoader) {
            this.streamLoader = Objects.requireNonNull(streamLoader);
            return this;
        }

        public @NotNull Builder armors(@NotNull List<ArmorImage> armors) {
            this.armors = Objects.requireNonNull(armors);
            return this;
        }

        public @NotNull Builder armorTrims(@NotNull List<ArmorImage> armors) {
            this.armorTrims = Objects.requireNonNull(armors);
            return this;
        }

        public @NotNull Builder palettes(@NotNull List<ArmorPaletteImage> armors) {
            this.palettes = Objects.requireNonNull(armors);
            return this;
        }

        public @NotNull Builder namespace(@NotNull String namespace) {
            this.namespace = Objects.requireNonNull(namespace);
            return this;
        }

        public @NotNull Builder nameMapper(@NotNull ArmorNameMapper nameMapper) {
            this.nameMapper = Objects.requireNonNull(nameMapper);
            return this;
        }

        public @NotNull Builder flush(boolean flush) {
            this.flush = flush;
            return this;
        }

        public @NotNull ArmorModel build() {
            try (var context = new ArmorModelContext(
                    Arrays.stream(ArmorResource.values())
                            .map(value -> new ArmorDataContext(value, streamLoader))
                            .toList(),
                    namespace,
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
