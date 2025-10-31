package kr.toxicity.library.armormodel.test;

import kr.toxicity.library.armormodel.ArmorImage;
import kr.toxicity.library.armormodel.ArmorModel;
import kr.toxicity.library.armormodel.ArmorPaletteImage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        var armor = ArmorModel.builder()
                .streamLoader(path -> Objects.requireNonNull(getResource(path)))
                .armors(List.of(new ArmorImage(
                        "copper",
                        loadImage("armor_test/copper_armor.png"),
                        loadImage("armor_test/copper_armor_leggings.png")
                )))
                .armorTrims(List.of(new ArmorImage(
                        "bolt",
                        loadImage("armor_trim_test/bolt_armor.png"),
                        loadImage("armor_trim_test/bolt_armor_leggings.png")
                )))
                .palettes(List.of(new ArmorPaletteImage("amethyst", loadImage("palette_test/amethyst.png"))))
                .build();

        var buildPath = getDataPath().resolve("build");
        armor.builders()
                .parallelStream()
                .forEach(builder -> {
                    var file = buildPath.resolve(builder.path()).toFile();
                    file.getParentFile().mkdirs();
                    try (
                            var output = new FileOutputStream(file);
                            var buffered = new BufferedOutputStream(output)
                    ) {
                        buffered.write(builder.get());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        var itemStacks = armor.resources()
                .values()
                .stream()
                .map(result -> {
                    var item = new ItemStack(Material.DIAMOND);
                    var modelData = result.customModelData("copper", "bolt", armor.colors().get("amethyst"));
                    item.editMeta(meta -> {
                        meta.setItemModel(NamespacedKey.fromString(modelData.itemModel().toString()));
                        var data = meta.getCustomModelDataComponent();
                        data.setStrings(modelData.strings());
                        data.setColors(modelData.colors().intStream().mapToObj(Color::fromRGB).toList());
                        meta.setCustomModelDataComponent(data);
                    });
                    return item;
                })
                .toArray(ItemStack[]::new);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void join(@NotNull PlayerJoinEvent event) {
                event.getPlayer().getInventory().addItem(itemStacks);
            }
        }, this);
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    private @NotNull BufferedImage loadImage(@NotNull String path) {
        try (var stream = Objects.requireNonNull(getResource(path))) {
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
