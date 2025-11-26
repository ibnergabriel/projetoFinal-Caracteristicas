package com.proyetogrupo.proyetofinal.negocio.util;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^(.+)@(.+)$");
    private ValidationUtil(){}
    public static boolean isBlank(String s){ return s == null || s.isBlank(); }
    public static boolean isValidEmail(String s){ return s != null && EMAIL.matcher(s).matches(); }
}