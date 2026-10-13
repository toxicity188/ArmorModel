/*
 * This source file is part of ArmorModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.library.armormodel;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Strategy record for transforming texture and model asset names.
 * <p>
 * Enables custom file naming conventions, obfuscation, or namespacing when generating
 * client resource pack assets.
 * <p>
 * Example:
 * <pre>{@code
 * ArmorNameMapper mapper = new ArmorNameMapper(
 *     tex -> "custom_tex_" + tex,
 *     mdl -> "custom_mdl_" + mdl
 * );
 * }</pre>
 *
 * @param textures transformation function for texture file names
 * @param models transformation function for model file names
 * @since 1.2.0
 */
public record ArmorNameMapper(
    @NotNull Function<String, String> textures,
    @NotNull Function<String, String> models
) {
    /**
     * Default identity name mapper that retains original file names unchanged.
     */
    public static final ArmorNameMapper DEFAULT = new ArmorNameMapper(
        s -> s,
        s -> s
    );
}
