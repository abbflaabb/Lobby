package com.abbas.lobby.Utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {
    private static final String WITH_COLOR = "((?<=%1$s)|(?=%1$s))";

    public static String translateColorCodes(String text) {
        String[] texts = text.split(String.format(WITH_COLOR, "&"));
        StringBuilder newText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                newText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
            } else {
                newText.append(texts[i]);
            }
        }
        return newText.toString();
    }
    public static List<String> translateColorCodes(List<String> lines) {
        List<String> coloredLines = new ArrayList<>();


        for (String line: lines) {
            coloredLines.add(translateColorCodes(line));
        }
        return coloredLines;
    }
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
