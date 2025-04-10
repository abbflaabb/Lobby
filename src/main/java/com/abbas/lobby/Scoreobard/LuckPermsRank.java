package com.abbas.lobby.Scoreobard;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsRank {
    private static LuckPerms luckPerms;

    public static void setup() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
    }

    public static String getPlayerRank(Player player) {
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
}
