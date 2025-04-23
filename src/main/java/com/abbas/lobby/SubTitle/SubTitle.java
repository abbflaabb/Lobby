package com.abbas.lobby.SubTitle;

import com.abbas.lobby.Placeholders.Placeholders;
import com.abbas.lobby.API.ISubTitle;
import com.abbas.lobby.Scoreboard.LuckPermsRank;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SubTitle implements ISubTitle {
    private final Placeholders placeholders;

    public SubTitle() {
        this.placeholders = new Placeholders(new LuckPermsRank());
        setupConfig();
    }


    @Override
    public void setupConfig() {
        Config.setup();
        FileConfiguration config = Config.getConfig();

        if (!config.isConfigurationSection("titles")) {
            config.set("titles.welcome.title", "&6&l%Lobby_rank%");
            config.set("titles.welcome.subtitle", "&7Welcome &b%player_name%");
            config.set("titles.welcome.fadeIn", 20);
            config.set("titles.welcome.stay", 60);
            config.set("titles.welcome.fadeOut", 20);

            Config.save();
        }
    }
    @Override
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

    @Override
    public void sendTitlePacket(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
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
    @Override
    public void clearTitle(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutTitle clearPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
        craftPlayer.getHandle().playerConnection.sendPacket(clearPacket);
    }
}
