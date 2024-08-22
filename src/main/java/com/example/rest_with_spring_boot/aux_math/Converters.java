package com.example.rest_with_spring_boot.aux_math;

public class Converters {
    
    public static boolean isNumeric(String strNum) {
        if (strNum == null) return false;
        String number = strNum.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertToDouble(String strNum) {
        if (strNum == null) return 0D;
        String number = strNum.replaceAll(",", ".");
        if (isNumeric(number)) return Double.valueOf(number);
        return 0D;
    }
}
