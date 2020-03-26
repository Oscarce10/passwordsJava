/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import conexion.ConexionMysql;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class AutenticarSalt {
    
    public static final ConexionMysql con = ConexionMysql.getInstance();
    
    public static void main(String[] args) {
        String usuario = "oscarce10";
        String data = "Hello World";
        String algorithm = "MD5"; 
        
        try {
            PreparedStatement ps = con.getCon().prepareStatement("SELECT * FROM usuario WHERE usuario LIKE ?");
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Clave original: " + rs.getString("clave"));
                byte[] salt = rs.getBytes("salt");
                System.out.println("Clave generada: " + generateHash(data, algorithm, salt));
                if(rs.getString("clave").equals(generateHash(data, algorithm, salt))){
                    System.out.println("USUARIO AUTENTICADO");
                }else{
                    System.out.println("USUARIO NO AUTENTICADO");
                }
            }else{
                System.out.println("USUARIO NO EXISTE");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutenticarSalt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AutenticarSalt.class.getName()).log(Level.SEVERE, null, ex);
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
    
}
