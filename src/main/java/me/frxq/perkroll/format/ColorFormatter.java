package me.frxq.perkroll.format;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFormatter {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

    public static String format(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);
        Matcher matcher = HEX_PATTERN.matcher(string);

        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            String chatColor = hexToChatColor(hex);

            matcher.appendReplacement(builder, Matcher.quoteReplacement(chatColor));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }
    public static String hexToChatColor(String hex) {
        StringBuilder colorCode = new StringBuilder("§x");
        for (char c : hex.toCharArray()) {
            colorCode.append("§").append(c);
        }
        return colorCode.toString();
    }
}
