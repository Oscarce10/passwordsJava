/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author USER
 */
public class Hashing {
    
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String data = "12345Password1";
        String algorithm = "MD5";
        System.out.println(algorithm + " Hash of " + data + ":  " + generateHash(data, algorithm));         
        

    }
    
    
    
    public static String generateHash (String data, String algorithm) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        byte[] hash = digest.digest(data.getBytes());
        return bytesToString(hash);
    }
    
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public static String bytesToString(byte[] bytes){
        char [] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    
    
}
