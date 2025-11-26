package com.proyetogrupo.proyetofinal.negocio.util;
import java.security.MessageDigest;

public final class SecurityUtil {
    private SecurityUtil(){}
    public static String sha256Hex(String input){ try{ MessageDigest md = MessageDigest.getInstance("SHA-256"); byte[] b = md.digest(input.getBytes()); StringBuilder sb = new StringBuilder(); for(byte x: b) sb.append(String.format("%02x", x)); return sb.toString(); } catch(Exception e){ throw new RuntimeException(e);} }
    public static boolean verify(String raw, String stored){ if(stored==null) return false; return stored.equals(raw) || stored.equalsIgnoreCase(sha256Hex(raw)); }
}