/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents raw geometry and texture mappings for a Minecraft armor bone model.
 * <p>
 * Manages texture references and cube element definitions used when decomposing armor parts
 * for packet-based entity rendering and client item models.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorData filtered = data.ofTextures("armormodel:item/armor/copper_armor", "armormodel:item/armor/bolt_armor_1");
 * String json = filtered.toJson();
 * }</pre>
 *
 * @param textures map of texture keys (e.g., "0", "1", "particle") to texture paths
 * @param elements list of model geometry cube elements
 * @since 1.2.0
 */
public record ArmorData(
    @NotNull Map<String, String> textures,
    @NotNull List<JsonElement> elements
) {
    /**
     * Shared {@link Gson} instance for serializing and deserializing armor model definitions.
     */
    public static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .create();

    /**
     * Serializes this armor model data into JSON format.
     *
     * @return JSON string representing this model
     */
    public @NotNull String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Filters bone geometry elements according to provided armor and armor trim texture keys.
     *
     * @param armor base armor texture key, or null to exclude layer 0 elements
     * @param armorTrim armor trim texture key, or null to exclude layer 1 trim elements
     * @return new {@link ArmorData} containing only elements referencing available textures
     */
    public @NotNull ArmorData ofTextures(@Nullable String armor, @Nullable String armorTrim) {
        var map = new HashMap<String, String>(3);
        if (armor != null) map.put("0", armor);
        if (armorTrim != null) map.put("1", armorTrim);
        map.put("particle", armor != null ? armor : armorTrim);
        return new ArmorData(
            Collections.unmodifiableMap(map),
            elements.stream()
                .filter(element -> map.keySet().containsAll(textureIndex(element)))
                .toList()
        );
    }

    private static @NotNull Set<String> textureIndex(@NotNull JsonElement element) {
        return Objects.requireNonNull(element.getAsJsonObject().get("faces"))
            .getAsJsonObject()
            .entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .map(v -> Objects.requireNonNull(v.getAsJsonObject().get("texture")).getAsString())
            .map(s -> s.substring(1))
            .collect(Collectors.toUnmodifiableSet());
    }
}
