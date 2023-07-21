package com.mindhub.homebanking.utils;

import static com.mindhub.homebanking.utils.NumberUtils.getRandomNumber;

public final class CardUtils {

    public static String getCardNumber() {
        return getRandomNumber(0, 9999) + "-" + getRandomNumber(0, 9999) + "-" + getRandomNumber(0, 9999) + "-" + getRandomNumber(0, 9999);
    }

    public static short getCvv() {
        return Short.parseShort(getRandomNumber(0, 999));
    }
}
