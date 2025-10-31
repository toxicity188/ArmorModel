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

    @NotNull ArmorDataResult build(@NotNull String namespace) {
        return new ArmorDataResult(
                new ArmorNamespaceKey(namespace, "armor/" + resource.name().toLowerCase()),
                armors.isEmpty() ? null : selectJson(0, armors),
                armorTrims.isEmpty() ? null : selectJson(1, armorTrims)
        );
    }
}
