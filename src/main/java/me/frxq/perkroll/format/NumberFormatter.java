package me.frxq.perkroll.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    public static String[] suffix = new String[]{"", "k", "m", "B", "T", "Q", "Qt"};

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);

    public static String format(byte number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(short number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(int number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(long number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(float number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(double number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String formatNumber(long number) {
        if (number < 1000000) {
            return format(number);
        }
        return formatLong(number);
    }

    public static String shrink(String type) {
        return type.substring(0, type.indexOf("."));
    }

    public static String formatLong(long number) {
        String formatted = new DecimalFormat("##0E0").format(number);
        int exponent = Integer.parseInt(formatted.substring(formatted.indexOf('E') + 1));
        int power = exponent / 3;
        if (power < suffix.length) {
            formatted = formatted.replaceAll("E[0-9]+", suffix[power]);
        } else {
            formatted = formatted.replaceAll("E[0-9]+", "E" + (power * 3));
        }
        while (formatted.length() > 4 || formatted.matches("[0-9]+\\.[a-zA-Z]+")) {
            formatted = formatted.substring(0, formatted.length() - 2) + formatted.substring(formatted.length() - 1);
        }
        return formatted;
    }
}
