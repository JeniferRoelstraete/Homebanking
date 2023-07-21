package com.mindhub.homebanking.utils;

public class NumberUtils {
    public static String getRandomNumber(int min, int max) {
        String generatedRandomNumber = String.valueOf((int)(Math.random() * (max - min)) + min); //3 -> completalo con "0" para llegar a la longitud deseada
        Integer desiredLength = String.valueOf(max).length(); // cvv -> 3 dígitos

        return padLeft(generatedRandomNumber, desiredLength, '0');
    }

    public static String padLeft(String text, int length, char replaceCharacter) {
        StringBuilder sb = new StringBuilder();
        if (text.length() < length) {
            sb.append(text);
            for (int i = text.length(); i < length; i++) {
                sb.append(replaceCharacter);
            }
            return sb.toString();
        }
        return text;
    }
}
