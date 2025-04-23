package com.abbas.lobby.TeleportBow;

import com.abbas.lobby.API.ITeleportAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleportBow implements ITeleportAPI {

    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.contains("teleport-bow")) {
            config.set("teleport-bow.name", "&b&lTeleport Bow");
            config.set("teleport-bow.lore", new String[]{
                    "&7Right click to teleport to",
                    "&7the location you are looking at."
            });
            config.set("teleport-bow.arrow-speed", 2.0);
            config.set("teleport-bow.enabled", true);
            Config.save();
        }
    }

    @Override
    public ItemStack createTeleportBow() {
        FileConfiguration config = Config.getConfig();
        String name = config.getString("teleport-bow.name", "&b&lTeleport Bow");
        List<String> lore = config.getStringList("teleport-bow.lore");

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        meta.setDisplayName(ColorUtils.translateColorCodes(name));

        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(ColorUtils.translateColorCodes(line));
        }
        meta.setLore(coloredLore);

        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        bow.setItemMeta(meta);
        return bow;
    }

    @Override
    public double getArrowSpeed() {
        return Math.min(Config.getConfig().getDouble("teleport-bow.arrow-speed", 2.0), 4.0);
    }

    @Override
    public boolean isEnabled() {
        return Config.getConfig().getBoolean("teleport-bow.enabled", true);
    }

    @Override
    public void giveTeleportBowItems(Player player) {
        player.getInventory().setItem(0, createTeleportBow());
        player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
        player.updateInventory();
    }

    @Override
    public boolean isTeleportBow(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return false;
        }
        String configName = ColorUtils.translateColorCodes(
                Config.getConfig().getString("teleport-bow.name", "&b&lTeleport Bow")
        );
        return item.getItemMeta().getDisplayName().equals(configName);
    }
}