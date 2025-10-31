package kr.toxicity.library.armormodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public @NotNull ArmorData ofTextures(@NotNull String... textures) {
        var map = new HashMap<String, String>();
        var i = 0;
        for (String texture : textures) {
            map.put(Integer.toString(i++), texture);
        }
        return new ArmorData(
                Collections.unmodifiableMap(map),
                elements
        );
    }
}
