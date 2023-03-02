package com.projekt.zaliczeniowy.penny_pincher.converter.type;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class BigDecimalConverter {

    @TypeConverter
    public static String bigDecimalToString(BigDecimal input) {
        return input.toPlainString();
    }

    @TypeConverter
    public static BigDecimal stringToBigDecimal(String input) {
        if (input.isEmpty()) return BigDecimal.valueOf(0.0);
        return new BigDecimal(input);
    }
}
