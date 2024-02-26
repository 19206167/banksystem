package com.nus.team4.util;

import java.security.SecureRandom;

public class AccountUtil {
    public static String generateAccountNumber() {
        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(num.length);
            sb.append(num[n]);
        }

        String iban = sb.toString();

        return iban;

    }

    public static String generateCVC() {
        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int n = random.nextInt(num.length);
            sb.append(num[n]);
        }

        String cvc = sb.toString();

        return cvc;
    }
}
