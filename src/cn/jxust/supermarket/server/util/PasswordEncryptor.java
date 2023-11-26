package cn.jxust.supermarket.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryptor {
    public static String encrypt(String password, String salt) {
        try {
            // 将盐与密码合并
            String passwordAndSalt = password + salt;
            // 获取 MessageDigest 实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将密码和盐进行加密
            md.update(passwordAndSalt.getBytes());
            byte[] bytes = md.digest();
            // 将加密后的结果转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
