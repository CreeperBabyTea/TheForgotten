package dev.pages.creeperbabytea.the_forgotten.api.util;

public class Formatting {
    private static final String[][] ROMAN_NUMBERS = {
            {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},
            {"X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"}
    };

    public static String convertToRoman(int num) {
        if (num >= 100)
            return "" + num;
        StringBuilder ret = new StringBuilder();
        for (int i = 0; num >= 10; ) {
            ret.append(ROMAN_NUMBERS[i][num % 10]);
            num /= 10;
        }
        return ret.toString();
    }
}
