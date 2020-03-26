/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import conexion.ConexionMysql;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class uuid {
    
    private static final ConexionMysql con = ConexionMysql.getInstance();
    
    public static void main(String[] args) {
        String id = UUID.randomUUID().toString();
        String usuario = "pdfuuid";        
              
        try {
            PreparedStatement ps = con.getCon().prepareStatement("INSERT INTO usuario (uuid, usuario, salt, clave) VALUES (UNHEX(REPLACE(?, '-', '')), ?, '', ?)");
            ps.setString(1, id);
            ps.setString(2, usuario);
            ps.setString(3, id);
            System.out.println(ps.toString());
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
    
}
