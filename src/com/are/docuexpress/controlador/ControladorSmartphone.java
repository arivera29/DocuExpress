/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Smartphone;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorSmartphone {
    private db conexion;
    private Smartphone smartphone;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Smartphone getSmartphone() {
        return smartphone;
    }

    public void setSmartphone(Smartphone smartphone) {
        this.smartphone = smartphone;
    }

    public ControladorSmartphone(db conexion) {
        this.conexion = conexion;
    }

    public ControladorSmartphone(db conexion, Smartphone smartphone) {
        this.conexion = conexion;
        this.smartphone = smartphone;
    }
    
    public boolean add(Smartphone smartphone) throws SQLException {
        boolean result=false;
        String sql = String.format("INSERT INTO smartphone (imei, estado, empresa) VALUES ('%s','%s','%s')", smartphone.getIMEI(),smartphone.getEstado(),smartphone.getEmpresa());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean update(Smartphone smartphone, String key) throws SQLException {
        boolean result=false;
        String sql = String.format("UPDATE smartphone SET imei='%s', estado='%s', empresa='%s' WHERE imei='%s'", smartphone.getIMEI(),smartphone.getEstado(),smartphone.getEmpresa(),key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean remove(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("DELETE smartphone WHERE imei='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT imei,estado,empresa FROM smartphone WHERE imei='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            smartphone = new Smartphone();
            smartphone.setIMEI(rs.getString("imei"));
            smartphone.setEstado(rs.getInt("estado"));
            smartphone.setEmpresa(rs.getString("empresa"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<Smartphone> list() throws SQLException {
        ArrayList<Smartphone> lista = new ArrayList<Smartphone>();
        String sql = "SELECT imei,estado,empresa FROM smartphone ORDER BY imei";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Smartphone smartphone = new Smartphone();
            smartphone.setIMEI(rs.getString("imei"));
            smartphone.setEstado(rs.getInt("estado"));
            smartphone.setEmpresa(rs.getString("empresa"));
            
            lista.add(smartphone);
        }
        
        return lista;
    }
    
    
}
