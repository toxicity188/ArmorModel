package kr.toxicity.library.armormodel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

final class ArmorUtil {

    public static final JsonObject EMPTY = new JsonObject();

    static {
        EMPTY.addProperty("type", "minecraft:empty");
    }

    private ArmorUtil() {
        throw new RuntimeException();
    }

    public static @NotNull JsonObject modelJson(@NotNull String modelName, int tint) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:model");
        json.addProperty("model", modelName);
        var tints = new JsonArray();
        var tintModelData = new JsonObject();
        tintModelData.addProperty("type", "minecraft:custom_model_data");
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
        var json = new JsonObject();
        json.addProperty("type", "minecraft:composite");
        json.add("models", array);
        return json;
    }

    public static @NotNull JsonObject selectJson(int index, @NotNull JsonArray array) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:select");
        json.addProperty("property", "minecraft:custom_model_data");
        json.addProperty("index", index);
        json.add("fallback", EMPTY);
        json.add("cases", array);
        return json;
    }
}
