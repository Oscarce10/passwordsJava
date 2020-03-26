/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import conexion.ConexionMysql;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class ConsultaUuid {
    
    private static final ConexionMysql con = ConexionMysql.getInstance();
    
    public static void main(String[] args) {
        String usuario = "oscarUuid";
        PreparedStatement ps;
        try {
            ps = con.getCon().prepareStatement("SELECT * FROM usuario WHERE usuario LIKE ?");
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                byte[] uuid = rs.getBytes("uuid");
                String id = bytesToString(uuid);
                System.out.println("UUID obtenido = " + id);
                System.out.println("UUID traido de bd = " + rs.getString("clave").replace("-", ""));
                if(bytesToString(rs.getBytes("uuid")).equalsIgnoreCase(rs.getString("clave").replace("-", ""))){
                    System.out.println("Los Uuid coinciden");
                }else{
                    System.out.println("Los Uuid no coinciden");
                }
            }else{
                System.out.println("USUARIO NO EXISTE");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaUuid.class.getName()).log(Level.SEVERE, null, ex);
        }

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
