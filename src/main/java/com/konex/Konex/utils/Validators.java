package com.konex.Konex.utils;

import com.konex.Konex.exception.BusinessException;

public class Validators {

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }
}
