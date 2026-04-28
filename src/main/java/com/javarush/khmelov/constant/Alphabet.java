package com.javarush.khmelov.constant;

import java.util.HashMap;
import java.util.Map;

public class Alphabet {

    private Alphabet() {
    }

    private static final String rus = "ЙЦУКЕНГШЩЗХЪЭЖДЛОРПАВЫФЯЧСМИТЬБЮ";
    private static final String symbols = "\n☮.,”’:-!? ";

    public static final char[] chars = (rus.toLowerCase() + symbols).toCharArray();

    public final static Map<Character, Integer> index = new HashMap<>();

    static {
        for (int i = 0; i < chars.length; i++) {
            index.put(chars[i], i);
        }
    }
}
