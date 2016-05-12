package com.soarex.bot.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ObsceneFilter {

    private static String regex = "\\b\\d*([\\wа-яА-Я]*[пПnPp][иИiI1uеЕ€eE][зЗ3zZ3][дДdD][\\wа-яА-Я]*|(?:[^иИiI1uуУyYuU\\s]+|нНhHиИiI1u)?(?<!стра)[хХxXhH][уУyYuUеЕ€eE][йЙyеЕ€eEяЯёЁиИiI1uлЛlLюЮрРpPrR](?!иг)[\\wа-яА-Я]*|[\\wа-яА-Я]*[бБ6bB][лЛlL](?:[яЯyYaA]+[дДdDтТtT]?|[иИiI1u]+[дДdDтТtT]+|[иИiI1u]+[аАaA]+)(?!х)[\\wа-яА-Я]*|(?:[\\wа-яА-Я]*[йЙyуУyYuUеЕ€eEаАaAоОoO0ъЪьЬыЫяЯ][еЕ€eEёЁяЯиИiI1u][бБ6bBпПnPp](?!ы\\b|ол)[\\wа-яА-Я]*|[еЕ€eEёЁ][бБ6bB][\\wа-яА-Я]*|[иИiI1u][бБ6bB][аАaA]\\w+|[йЙy][оОoO0][бБ6bBпПnPp][\\wа-яА-Я]*)|[\\wа-яА-Я]*[сСcCsS][цЦcC]?[уУyYuU]+(?:[чЧ4]*[кКkK]+|[чЧ4]+[кКkK]*)[аАaAоОoO0еЕ€ёЁ][\\wа-яА-Я]*|[\\wа-яА-Я]*(?:[пПnPp][иИiI1uеЕ€eE][дДdD][аАaAоОoO0еЕ€eE]?[рРpPrR](?!о)[\\wа-яА-Я]*|[пПnPp][еЕ€eE][дДdD][еЕ€eEиИiI1u]?[гГgGкКkK])|[\\wа-яА-Я]*[зЗ3zZ3][аАaAоОoO0][лЛlL][уУyYuU][пПnPp][\\wа-яА-Я]*|[гГgG][оОoO0аАaA][нНhH][дДdD][оОoO0][нНhH]*|[фФfF][аАaAuU][cCкКkK]*|[cC][оОoO0][cC][кКkK]*|[\\wа-яА-Я]*[гГgG][оОoO0аАaA][vVвВ][еЕ€eEнНhH][\\wа-яА-Я]*|[\\wа-яА-Я]*[сСcCsS][рРpPrR][аАaA][чЧ4кКkKтТtT][\\wа-яА-Я]*|[\\wа-яА-Я]*[пПnPp][иИiI1u][сСcCsS][юЮ][\\wа-яА-Я]*|[\\wа-яА-Я]*[fF][uU][cCсС][kKкК][\\wа-яА-Я]*|[\\wа-яА-Я]*[дДdD][рРpPrR][оОoO0][чЧ4][\\wа-яА-Я]*|[\\wа-яА-Я]*[бБ6bB][зЗ3zZ3][дДdD][\\wа-яА-Я]*|[\\wа-яА-Я]*[мМmM][аАaA][нНhH][дДdD][аАaAоОoO0][\\wа-яА-Я]*)\\b";
    private static Pattern pattern = Pattern.compile(regex);
    public static String[] exceptions = new String[]{"eun", "hup", "хел", "where", "махер", "bla", "блямс", "блямб", "ноябр", "мандат", "eup", "hyundai", "roup", "sound", "oun", "aube", "ibarg", "ebay", "eeb", "shuy", "cayenne", "ain", "oin", "uen", "uip", "oup", "боеп", "деепр", "хульс", "een", "ee6", "ein", "хулио", "ебэ", "перв", "eep", "ying", "laun", "чаепитие"};
    public static ArrayList<String> invective = new ArrayList<>();

    public static boolean isAllowed(String input) {
        return input.toLowerCase().replaceAll("[\\.,_\\-\\[\\]\\{\\}\\|\\\\\\/&?!\\`@#№$;:%\\^*\\(\\)]", "").equals(filterText(input));
    }

    public static String filterText(String input) {
        String text = input.toLowerCase().replaceAll("[\\.,_\\-\\[\\]\\{\\}\\|\\\\\\/&?!\\`@#№$;:%\\^*\\(\\)]", "");
        Matcher matcher = pattern.matcher(text);
        invective.clear();
        while(matcher.find()) {
            String word = matcher.group();
            for (String exclude : exceptions) {
                if (word.indexOf(exclude) != -1) {
                    word = "";
                    break;
                }
            }
            if (!word.isEmpty()) {
                invective.add(word);
                StringBuilder replacement = new StringBuilder();
                for (int i = 0; i < word.length(); i++)
                    replacement.append('*');
                text = text.replace(word, replacement);
            }
        }
        return text;
    }
}