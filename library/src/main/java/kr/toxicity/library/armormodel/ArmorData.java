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

public record ArmorData(
    @NotNull Map<String, String> textures,
    @NotNull List<JsonElement> elements
) {
    public static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .create();

    public @NotNull String toJson() {
        return GSON.toJson(this);
    }

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
