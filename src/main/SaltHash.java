/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import conexion.ConexionMysql;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class SaltHash {
    
    
    private static final ConexionMysql con = ConexionMysql.getInstance();
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String data = "Hello World";
        String algorithm = "MD5";
        byte[] salt = generateSalt();
        System.out.println(algorithm + " Hash of " + data + " and salt " + bytesToString(salt) + ":  " + generateHash(data, algorithm, salt));
        
        PreparedStatement ps;
        try {
            ps = con.getCon().prepareStatement("INSERT INTO usuario (usuario, salt, clave) VALUES (?, ?, ?)");
            ps.setString(1, "oscarce10");
            ps.setBytes(2, salt);
            ps.setString(3, generateHash(data, algorithm, salt));
            if(ps.executeUpdate()>0)
                System.out.println("Usuario registrado");
            else
                System.out.println("Usuario NO registrado");
        } catch (SQLException ex) {
            Logger.getLogger(Hashing.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            con.cerrarConexion();
        }
    }
    
    
    
    public static String generateHash (String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
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
    
    public static byte[] generateSalt(){
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }
    
    
}
