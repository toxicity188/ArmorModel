package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

final class ArmorModelContext implements AutoCloseable {

    final List<ArmorDataContext> data;
    final ArmorNamespace namespace;
    private final ArmorNameMapper nameMapper;
    private final boolean flush;

    final List<ArmorByteBuilder> builders = new ArrayList<>();
    private Map<ArmorResource, ArmorDataResult> results;

    ArmorModelContext(
            @NotNull List<ArmorDataContext> data,
            @NotNull String namespace,
            @NotNull ArmorNameMapper nameMapper,
            boolean flush
    ) {
        this.data = data;
        this.namespace = new ArmorNamespace(this, namespace);
        this.nameMapper = nameMapper;
        this.flush = flush;
    }

    void loadArmor(@NotNull List<ArmorImage> images) {
        for (var image : images) {
            ArmorImageProcessor.ARMOR.process(image, this);
        }
    }

    void loadArmorTrim(@NotNull List<ArmorImage> images) {
        for (var image : images) {
            ArmorImageProcessor.ARMOR_TRIM.process(image, this);
        }
    }

    public @NotNull Map<ArmorResource, ArmorDataResult> results() {
        if (results != null) return results;
        synchronized (this) {
            if (results != null) return results;
            return results = Collections.unmodifiableMap(data.stream()
                    .collect(Collectors.toMap(
                            c -> c.resource,
                            c -> c.build(namespace.namespace),
                            (f, s) -> s,
                            () -> new EnumMap<>(ArmorResource.class)
                    )));
        }
    }

    @NotNull String textureName(@NotNull String name) {
        return nameMapper.textures().apply(name);
    }

    @NotNull String modelName(@NotNull String name) {
        return nameMapper.models().apply(name);
    }

    @NotNull String textureNamespace(@NotNull String name) {
        return namespace.namespace + ":item/armor/" + textureName(name);
    }

    @NotNull String modelNamespace(@NotNull String name) {
        return namespace.namespace + ":armor/" + modelName(name);
    }

    @Override
    public void close() {
        if (!flush) return;
        for (var value : results().values()) {
            namespace.items.add(value.key().item() + ".json", value, v -> ArmorData.GSON.toJson(v.build()).getBytes(StandardCharsets.UTF_8));
        }
    }
}
