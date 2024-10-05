package com.example.SpringAppCrud.util;

import java.util.Base64;

public class ThymeleafUtil {

    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
