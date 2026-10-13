/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

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

/**
 * Compiled item model definition and model data generator for a specific armor bone.
 * <p>
 * Combines base armor and trim {@code select} cases into a single client model definition
 * and produces {@link ArmorCustomModelData} tokens for packet-based entity rendering and
 * {@code item_display} equipment.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorDataResult result = armorModel.resource(ArmorResource.CHEST);
 * JsonObject itemDefinition = result.build();
 * ArmorCustomModelData data = result.customModelData("copper", 0xFFFFFF, "bolt", palette);
 * }</pre>
 *
 * @param key namespaced key identifying the item definition
 * @param armor select model JSON for base armor layers, or null if absent
 * @param armorTrim composite select model JSON for armor trims, or null if absent
 * @since 1.2.0
 */
public record ArmorDataResult(
    @NotNull ArmorNamespaceKey key,
    @Nullable JsonObject armor,
    @Nullable JsonObject armorTrim
) {

    /**
     * Default white RGB tint color index (0xFFFFFF).
     */
    public static final int WHITE_INDEX = 0xFFFFFF;

    /**
     * Wraps the compiled model JSON into a root Minecraft 1.21.4+ item model definition.
     *
     * @return root item definition JSON object with a "model" entry
     */
    public @NotNull JsonObject build() {
        var json = new JsonObject();
        json.add("model", toJson());
        return json;
    }

    /**
     * Compiles armor and trim model selectors into a single model JSON object.
     *
     * @return composite, single, or empty model JSON object
     */
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

    /**
     * Creates custom model data for an untinted base armor type without trim.
     *
     * @param type armor identifier string matched in the item model definition
     * @return custom model data configured for untinted armor
     */
    public @NotNull ArmorCustomModelData customModelData(@NotNull String type) {
        return customModelData(type, WHITE_INDEX);
    }

    /**
     * Creates custom model data for a tinted base armor type without trim.
     *
     * @param type armor identifier string matched in the item model definition
     * @param armorTint 24-bit RGB tint color applied to the base armor model layer
     * @return custom model data configured with armor tint
     */
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

    /**
     * Creates custom model data for an untinted base armor type with an armor trim.
     *
     * @param type armor identifier string matched in the item model definition
     * @param trim armor trim pattern identifier string
     * @param palette color palette used to tint the 8 trim shading layers, or null for untrimmed
     * @return custom model data configured with armor and trim
     */
    public @NotNull ArmorCustomModelData customModelData(@NotNull String type, @NotNull String trim, @Nullable ArmorTrimPalette palette) {
        return customModelData(type, WHITE_INDEX, trim, palette);
    }

    /**
     * Creates custom model data for a tinted base armor type with an armor trim and color palette.
     *
     * @param type armor identifier string matched in the item model definition
     * @param armorTint 24-bit RGB tint color applied to the base armor model layer
     * @param trim armor trim pattern identifier string
     * @param palette color palette used to tint the 8 trim shading layers, or null to omit trim colors
     * @return custom model data configured with armor tint and trim palette
     */
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
