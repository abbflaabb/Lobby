package com.abbas.lobby.Scoreboard;

import com.abbas.lobby.API.MainAPIS.ILuckPerms;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsRank implements ILuckPerms {
    private static LuckPerms luckPerms;

    public void setup() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
    }

    public String getPlayerRank(Player player) {
        if (luckPerms == null) {
            return "No Rank";
        }

        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return "No Rank";
        }

        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null ? prefix : user.getPrimaryGroup();
    }

    /**
     * Gets the player's prefix from LuckPerms
     *
     * @param player The player to get the prefix for
     * @return The player's prefix or null if not found
     */
    @Override
    public String getPlayerPrefix(Player player) {
        return "";
    }
}
