/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import com.google.gson.JsonArray;
import org.jetbrains.annotations.NotNull;

import static kr.toxicity.library.armormodel.ArmorUtil.selectJson;

final class ArmorDataContext {
    final ArmorResource resource;
    final ArmorData data;
    final JsonArray armors = new JsonArray();
    final JsonArray armorTrims = new JsonArray();

    ArmorDataContext(@NotNull ArmorResource resource, @NotNull ArmorResource.StreamLoader streamLoader) {
        this.resource = resource;
        this.data = resource.read(streamLoader);
    }

    @NotNull ArmorDataResult build(@NotNull String namespace, @NotNull String itemPath) {
        return new ArmorDataResult(
            new ArmorNamespaceKey(namespace, itemPath, resource.name().toLowerCase()),
            armors.isEmpty() ? null : selectJson(0, armors),
            armorTrims.isEmpty() ? null : selectJson(1, armorTrims)
        );
    }
}
