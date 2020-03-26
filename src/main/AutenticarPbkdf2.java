/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import conexion.ConexionMysql;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author USER
 */
public class AutenticarPbkdf2 {
    
    public static final ConexionMysql con = ConexionMysql.getInstance();
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String usuario = "pdf";
        String data = "1";
        int iterations = 65000;
        int keyLength = 256;
        
        try {
            PreparedStatement ps = con.getCon().prepareStatement("SELECT * FROM usuario WHERE usuario LIKE ?");
            ps.setString(1, usuario);
            System.out.println(ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                byte[] salt = rs.getBytes("salt");
                char[] hash = data.toCharArray();
                if(rs.getString("clave").equals(generateHash(hash, salt, iterations, keyLength))){
                    System.out.println("USUARIO AUTENTICADO");
                }else{
                    System.out.println("USUARIO NO AUTENTICADO");
                }
            }else{
                System.out.println("USUARIO NO EXISTE");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutenticarPbkdf2.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            con.cerrarConexion();
        }        
        
    }
    
    public static String generateHash (char[] data, byte[] salt, int iterations, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(data, salt, keyLength, keyLength);
        SecretKey key = skf.generateSecret(spec);
        byte[] res = key.getEncoded();        
        return bytesToString(res);
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
