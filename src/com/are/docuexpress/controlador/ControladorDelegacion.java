/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Delegacion;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorDelegacion {
    private db conexion ;
    private Delegacion delegacion;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Delegacion getDelegacion() {
        return delegacion;
    }

    public void setDelegacion(Delegacion delegacion) {
        this.delegacion = delegacion;
    }

    
    public ControladorDelegacion(db conexion) {
        this.conexion = conexion;
    }

    public ControladorDelegacion(db conexion, Delegacion delegacion) {
        this.conexion = conexion;
        this.delegacion = delegacion;
    }
    
    public boolean add(Delegacion delegacion) throws SQLException {
        boolean result=false;
        String sql = String.format("INSERT INTO delegacion (codDelegacion, nomDelegacion) VALUES ('%s','%s')", delegacion.getCodigo(), delegacion.getNombre());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean update(Delegacion delegacion, String key) throws SQLException {
        boolean result=false;
        String sql = String.format("UPDATE delegacion SET codDelegacion='%s', nomDelegacion='%s' WHERE codDelegacion='%s'", delegacion.getCodigo(), delegacion.getNombre(),key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean remove(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("DELETE delegacion WHERE codDelegacion='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT codDelegacion,nomDelegacion FROM delegacion WHERE codDelegacion='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            delegacion = new Delegacion();
            delegacion.setCodigo(rs.getString("codDelegacion"));
            delegacion.setNombre(rs.getString("nomDelegacion"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<Delegacion> list() throws SQLException {
        ArrayList<Delegacion> lista = new ArrayList<Delegacion>();
        String sql = "SELECT codDelegacion,nomDelegacion FROM delegacion ORDER BY nomDelegacion";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Delegacion delegacion = new Delegacion();
            delegacion.setCodigo(rs.getString("codDelegacion"));
            delegacion.setNombre(rs.getString("nomDelegacion"));
            
            lista.add(delegacion);
        }
        
        return lista;
    }
    
}
