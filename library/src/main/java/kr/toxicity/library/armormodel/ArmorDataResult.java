package kr.toxicity.library.armormodel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static kr.toxicity.library.armormodel.ArmorUtil.EMPTY;
import static kr.toxicity.library.armormodel.ArmorUtil.compositeJson;

public record ArmorDataResult(
        @NotNull ArmorNamespaceKey key,
        @Nullable JsonObject armor,
        @Nullable JsonObject armorTrim
) {

    public static final int WHITE_INDEX = 0xFFFFFF;

    public @NotNull JsonObject build() {
        var json = new JsonObject();
        json.add("model", toJson());
        return json;
    }

    public @NotNull JsonObject toJson() {
        if (armor != null && armorTrim != null) {
            var array = new JsonArray();
            array.add(armor);
            array.add(armorTrim);
            return compositeJson(array);
        } else if (armor != null) {
            return armor;
        } else return armorTrim != null ? armorTrim : EMPTY;
    }

    public @NotNull ArmorCustomModelData customModelData(@NotNull String type) {
        return customModelData(type, WHITE_INDEX);
    }

    public @NotNull ArmorCustomModelData customModelData(@NotNull String type, int armorTint) {
        return new ArmorCustomModelData(key, Collections.singletonList(type), IntList.of(
                armorTint,
                WHITE_INDEX,
                WHITE_INDEX,
                WHITE_INDEX,
                WHITE_INDEX,
                WHITE_INDEX,
                WHITE_INDEX,
                WHITE_INDEX,
                WHITE_INDEX
        ));
    }

    public @NotNull ArmorCustomModelData customModelData(@NotNull String type, @NotNull String trim, @Nullable ArmorTrimPalette palette) {
        return customModelData(type, WHITE_INDEX, trim, palette);
    }

    public @NotNull ArmorCustomModelData customModelData(@NotNull String type, int armorTint, @NotNull String trim, @Nullable ArmorTrimPalette palette) {
        if (palette == null) return customModelData(type, armorTint);
        var list = new IntArrayList(9);
        list.add(armorTint);
        for (ArmorColor armorColor : palette.palettes()) {
            list.add(armorColor.rgb());
        }
        return new ArmorCustomModelData(key, List.of(type,trim), list);
    }
}
