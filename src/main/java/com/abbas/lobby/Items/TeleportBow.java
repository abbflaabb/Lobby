package com.abbas.lobby.Items;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
public class TeleportBow {
    public static void setup() {
        Config.setup();
        FileConfiguration config =  Config.getConfig();

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

    public static ItemStack InventoryBow() {
        FileConfiguration config = Config.getConfig();
        String name = config.getString("teleport-bow.name", "&b&lTeleport Bow");
        List<String> lore = config.getStringList("teleport-bow.lore");

        ItemStack Bow =  new ItemStack(Material.BOW);
        ItemMeta meta = Bow.getItemMeta();

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

        Bow.setItemMeta(meta);
        return Bow;
    }
}
