package com.abbas.lobby.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFilterConfig {
    private static File file;
    private static FileConfiguration customFile;
    private static Map<String, String> wordPunishments = new HashMap<>();

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Lobby").getDataFolder(), "chatfilter.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                customFile = YamlConfiguration.loadConfiguration(file);

                customFile.set("punishments.minor.mute", "30m");
                customFile.set("punishments.minor.message", "&cPlease watch your language!");
                customFile.set("punishments.minor.commands", Arrays.asList(
                        "warn %player% Minor language violation",
                        "broadcast &c%player% has been muted for inappropriate language"
                ));

                customFile.set("punishments.severe.mute", "1h");
                customFile.set("punishments.severe.message", "&4This type of language is not tolerated on our server!");
                customFile.set("punishments.severe.commands", Arrays.asList(
                        "warn %player% Severe language violation",
                        "broadcast &4%player% has been muted for severe inappropriate language"
                ));

                customFile.set("bannedWords", Arrays.asList(
                        "immoral",
                        "wtf",
                        "shit, minor",
                        "kys, severe",
                        "fuck, severe",
                        "pene, minor",
                        "p e n e, minor",
                        "www.",
                        "https://",
                        "WWW.",
                        "HTTPS://",
                        "http://",
                        "http",
                        "HTTP://",
                        "gay, minor",
                        "lgbt, minor",
                        "racist, severe",
                        "hp",
                        "hdp, severe",
                        "HP",
                        "hijueputa, severe",
                        "mlp",
                        "m l p",
                        "malparido, severe",
                        "m a l p a r i d o, severe",
                        "h p",
                        "culo, minor",
                        "kulo, minor",

                        "kcuf, severe",
                        "enep, minor",
                        "e n e p, minor",
                        ".www",
                        "//:sptth",
                        ".WWW",
                        "//:SPTTH",
                        "//:ptth",
                        "ptth",
                        "//:PTTH",
                        "yag, minor",
                        "tbgL, minor",
                        "tsicar, severe",
                        "ph",
                        "pdh, severe",
                        "PH",
                        "atupeujih, severe",
                        "plm",
                        "p l m",
                        "odiraplam, severe",
                        "o d i r a p l a m, severe",
                        "p h",
                        "oluc, minor",
                        "oluk, minor"
                ));

                // General filter settings
                customFile.set("settings.enabled", true);
                customFile.set("settings.ignoreSpaces", true);
                customFile.set("settings.ignoreCaps", true);
                customFile.set("settings.defaultAction", "none");

                customFile.save(file);
            } catch (IOException e) {
                System.out.println("Couldn't create chatfilter.yml");
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        loadWordPunishments();
    }

    public static FileConfiguration getConfig() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save chatfilter.yml");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
        loadWordPunishments();
    }

    private static void loadWordPunishments() {
        wordPunishments.clear();
        List<String> bannedWords = customFile.getStringList("bannedWords");

        for (String entry : bannedWords) {
            String[] parts = entry.split(", ", 2);
            String word = parts[0].trim().toLowerCase();
            String punishment = parts.length > 1 ? parts[1].trim() : customFile.getString("settings.defaultAction", "none");
            wordPunishments.put(word, punishment);
        }
    }

    public static String checkMessage(String message) {
        if (!customFile.getBoolean("settings.enabled", true)) {
            return "none";
        }

        boolean ignoreSpaces = customFile.getBoolean("settings.ignoreSpaces", true);
        boolean ignoreCaps = customFile.getBoolean("settings.ignoreCaps", true);

        String processedMessage = message;
        if (ignoreCaps) {
            processedMessage = processedMessage.toLowerCase();
        }
        if (ignoreSpaces) {
            processedMessage = processedMessage.replace(" ", "");
        }

        String highestPunishment = "none";
        int punishmentPriority = -1;

        for (Map.Entry<String, String> entry : wordPunishments.entrySet()) {
            String bannedWord = entry.getKey();
            String punishment = entry.getValue();

            if (punishment.equals("none")) {
                continue;
            }

            // Check if contains the word
            boolean contains = false;
            if (ignoreSpaces) {
                bannedWord = bannedWord.replace(" ", "");
            }
            if (ignoreCaps) {
                bannedWord = bannedWord.toLowerCase();
            }

            if (processedMessage.contains(bannedWord)) {
                contains = true;
            }

            if (contains) {
                int newPriority = getPunishmentPriority(punishment);
                if (newPriority > punishmentPriority) {
                    highestPunishment = punishment;
                    punishmentPriority = newPriority;
                }
            }
        }

        return highestPunishment;
    }


    private static int getPunishmentPriority(String punishment) {
        switch (punishment.toLowerCase()) {
            case "severe": return 2;
            case "minor": return 1;
            default: return 0;
        }
    }

    public static String getMuteDuration(String punishmentLevel) {
        return customFile.getString("punishments." + punishmentLevel + ".mute");
    }

    public static String getPunishmentMessage(String punishmentLevel) {
        return customFile.getString("punishments." + punishmentLevel + ".message");
    }


    public static List<String> getPunishmentCommands(String punishmentLevel) {
        return customFile.getStringList("punishments." + punishmentLevel + ".commands");
    }
}
