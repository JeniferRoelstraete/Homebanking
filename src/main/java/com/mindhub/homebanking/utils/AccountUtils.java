package com.mindhub.homebanking.utils;

import static com.mindhub.homebanking.utils.NumberUtils.getRandomNumber;

public final class AccountUtils {

    public static String getAccountNumber() {
        return "VIN-" + getRandomNumber(1, 99999999);
    }
}
