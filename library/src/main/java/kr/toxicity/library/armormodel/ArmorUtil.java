/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * Internal JSON construction utilities for Minecraft 1.21.4+ item model definitions.
 */
final class ArmorUtil {

    public static final JsonObject EMPTY = new JsonObject();

    static {
        EMPTY.addProperty("type", "empty");
    }

    private ArmorUtil() {
        throw new RuntimeException();
    }

    public static @NotNull JsonObject modelJson(@NotNull String modelName, int tint) {
        var json = new JsonObject();
        json.addProperty("type", "model");
        json.addProperty("model", modelName);
        var tints = new JsonArray();
        var tintModelData = new JsonObject();
        tintModelData.addProperty("type", "custom_model_data");
        tintModelData.addProperty("index", tint);
        tintModelData.addProperty("default", ArmorDataResult.WHITE_INDEX);
        tints.add(tintModelData);
        json.add("tints", tints);
        return json;
    }

    public static @NotNull JsonObject whenJson(@NotNull String select, @NotNull JsonObject object) {
        var json = new JsonObject();
        json.addProperty("when", select);
        json.add("model", object);
        return json;
    }

    public static @NotNull JsonObject compositeJson(@NotNull JsonArray array) {
        if (array.size() == 1) return array.get(0).getAsJsonObject();
        var json = new JsonObject();
        json.addProperty("type", "composite");
        json.add("models", array);
        return json;
    }

    public static @NotNull JsonObject selectJson(int index, @NotNull JsonArray array) {
        var json = new JsonObject();
        json.addProperty("type", "select");
        json.addProperty("property", "custom_model_data");
        json.addProperty("index", index);
        json.add("fallback", EMPTY);
        json.add("cases", array);
        return json;
    }
}
