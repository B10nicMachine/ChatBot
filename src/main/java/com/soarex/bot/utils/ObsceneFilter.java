package com.soarex.bot.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ObsceneFilter {

    private static String regex = "\\b\\d*([\\wа-яА-Я]*[пПnPp][иИiI1uеЕeE][зЗ3zZ3][дДdD][\\wа-яА-Я]*|(?:[^иИiI1uуУyYuU\\s]+|нНhHиИiI1u)?(?<!стра)[хХxXhH][уУyYuUеЕeE][йЙyеЕeEяЯёЁиИiI1uлЛlLюЮрРpPrR](?!иг)[\\wа-яА-Я]*|[\\wа-яА-Я]*[бБ6bB][лЛlL](?:[яЯ]+[дДdDтТtT]?|[иИiI1u]+[дДdDтТtT]+|[иИiI1u]+[аАaA]+)(?!х)[\\wа-яА-Я]*|(?:[\\wа-яА-Я]*[йЙyуУyYuUеЕeEаАaAоОoO0ъЪьЬыЫяЯ][еЕeEёЁяЯиИiI1u][бБ6bBпПnPp](?!ы\\b|ол)[\\wа-яА-Я]*|[еЕeEёЁ][бБ6bB][\\wа-яА-Я]*|[иИiI1u][бБ6bB][аАaA]\\w+|[йЙy][оОoO0][бБ6bBпПnPp][\\wа-яА-Я]*)|[\\wа-яА-Я]*[сСcCsS][цЦcC]?[уУyYuU]+(?:[чЧ4]*[кКkK]+|[чЧ4]+[кКkK]*)[аАaAоОoO0еЕёЁ][\\wа-яА-Я]*|[\\wа-яА-Я]*(?:[пПnPp][иИiI1uеЕeE][дДdD][аАaAоОoO0еЕeE]?[рРpPrR](?!о)[\\wа-яА-Я]*|[пПnPp][еЕeE][дДdD][еЕeEиИiI1u]?[гГgGкКkK])|[\\wа-яА-Я]*[зЗ3zZ3][аАaAоОoO0][лЛlL][уУyYuU][пПnPp][\\wа-яА-Я]*|[\\wа-яА-Я]*[мМmM][аАaA][нНhH][дДdD][аАaAоОoO0][\\wа-яА-Я]*)\\b";
    private static Pattern pattern = Pattern.compile(regex);
    public static String[] exceptions = new String[]{"махер", "блямс", "ноябр", "мандат", "hyundai", "roup", "sound", "oun", "aube", "ibarg", "ebay", "eeb", "shuy", "cayenne", "ain", "oin", "uen", "uip", "oup", "боеп", "деепр", "хульс", "een", "ee6", "ein", "хулио", "ебэ", "перв", "eep", "ying", "laun", "чаепитие"};
    public static ArrayList<String> invective = new ArrayList<>();

    public static boolean isAllowed(String input) {
        return input.toLowerCase().equals(filterText(input));
    }

    public static String filterText(String input) {
        String text = input.toLowerCase();
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