package com.abbas.lobby.SubTitle;

import com.abbas.lobby.Placeholders.Placeholders;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SubTitle {
    private final Placeholders placeholders;

    public SubTitle() {
        this.placeholders = new Placeholders();
        setupConfig();
    }

    private void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("titles")) {
            config.set("titles.welcome.title", "&6&l%Lobby_rank%");
            config.set("titles.welcome.subtitle", "&7Welcome &b%player_name%");
            config.set("titles.welcome.fadeIn", 20);
            config.set("titles.welcome.stay", 60);
            config.set("titles.welcome.fadeOut", 20);

            config.set("titles.rank.title", "&b&l%Lobby_rank%");
            config.set("titles.rank.subtitle", "&7%player_name%");
            config.set("titles.rank.fadeIn", 10);
            config.set("titles.rank.stay", 40);
            config.set("titles.rank.fadeOut", 10);

            Config.save();
        }
    }

    public void sendTitle(Player player, String type) {
        FileConfiguration config = Config.getConfig();
        String titlePath = "titles." + type + ".";

        if (!config.isConfigurationSection(titlePath)) {
            return;
        }

        String title = ColorUtils.translateColorCodes(
                placeholders.replacePlaceholders(
                        config.getString(titlePath + "title", ""),
                        player
                )
        );

        String subtitle = ColorUtils.translateColorCodes(
                placeholders.replacePlaceholders(
                        config.getString(titlePath + "subtitle", ""),
                        player
                )
        );

        int fadeIn = config.getInt(titlePath + "fadeIn", 20);
        int stay = config.getInt(titlePath + "stay", 60);
        int fadeOut = config.getInt(titlePath + "fadeOut", 20);

        sendTitlePacket(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public void sendCustomTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        title = ColorUtils.translateColorCodes(placeholders.replacePlaceholders(title, player));
        subtitle = ColorUtils.translateColorCodes(placeholders.replacePlaceholders(subtitle, player));
        sendTitlePacket(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    private void sendTitlePacket(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        PacketPlayOutTitle timings = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        craftPlayer.getHandle().playerConnection.sendPacket(timings);

        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
        craftPlayer.getHandle().playerConnection.sendPacket(titlePacket);

        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}");
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent);
        craftPlayer.getHandle().playerConnection.sendPacket(subtitlePacket);
    }

    public void clearTitle(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutTitle clearPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
        craftPlayer.getHandle().playerConnection.sendPacket(clearPacket);
    }
}
