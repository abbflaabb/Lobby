package com.abbas.lobby.Placeholders;

import com.abbas.lobby.API.MainAPIS.ILuckPerms;
import com.abbas.lobby.API.MainAPIS.IPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Placeholders implements IPlaceholders {
    private final Map<String, Function<Player, String>> placeholders;
    private final ILuckPerms luckPerms;
    private long cooldownTime;

    public Placeholders(ILuckPerms luckPerms) {
        this.luckPerms = luckPerms;
        this.placeholders = new HashMap<>();
        setupDefaultPlaceholders();
        this.cooldownTime = 0;

    }

    private void setupDefaultPlaceholders() {
        placeholders.put("%player_name%", Player::getName);
        placeholders.put("%Lobby_rank%", player -> luckPerms.getPlayerRank(player));
        placeholders.put("%online_players%", player -> String.valueOf(Bukkit.getOnlinePlayers().size()));
        placeholders.put("%ip_server%", player -> Bukkit.getServer().getIp());
        placeholders.put("%cooldown_Time%", player -> String.valueOf(cooldownTime));
        placeholders.put("%Date_time%", player -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return now.format(formatter);
        });
    }


    @Override
    public String replacePlaceholders(String text, Player player) {
        String result = text;
        for (Map.Entry<String, Function<Player, String>> entry : placeholders.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue().apply(player));
        }
        return result;
    }

    @Override
    public void addPlaceholder(String placeholder, Function<Player, String> resolver) {
        placeholders.put(placeholder, resolver);
    }
    public void setCooldownTime(long time) {
        this.cooldownTime = time;
    }
}
