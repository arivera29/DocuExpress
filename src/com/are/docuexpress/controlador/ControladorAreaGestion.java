/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.AreaGestion;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorAreaGestion {
    private db conexion;
    private AreaGestion area;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public AreaGestion getArea() {
        return area;
    }

    public void setArea(AreaGestion area) {
        this.area = area;
    }

    public ControladorAreaGestion(db conexion) {
        this.conexion = conexion;
    }

    public ControladorAreaGestion(db conexion, AreaGestion area) {
        this.conexion = conexion;
        this.area = area;
    }
    
    public boolean add(AreaGestion area) throws SQLException {
        String sql = "INSERT INTO AreaGestion (nomArea,estArea) VALUES (?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, area.getNombre());
        pst.setInt(2, area.getEstado());
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }
    
    public boolean update(AreaGestion area, int key) throws SQLException {
        String sql = "UPDATE AreaGestion SET nomArea=?, estArea=? WHERE codArea=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, area.getNombre());
        pst.setInt(2, area.getEstado());
        pst.setInt(3, key);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        return false;
    }
    
    public boolean remove(int key) throws SQLException {
        String sql = "DELETE FROM AreaGestion WHERE codArea=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }

        
        return false;
    }
    
    public boolean find(int key) throws SQLException {
        String sql = "SELECT codArea, nomArea,estArea FROM AreaGestion WHERE codArea=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            area = new AreaGestion();
            area.setId(rs.getInt("codArea"));
            area.setNombre(rs.getString("nomArea"));
            area.setEstado(rs.getInt("estArea"));
            return true;
        }

        
        return false;
    }
    
    public ArrayList<AreaGestion> list() throws SQLException {
        ArrayList<AreaGestion> lista = new ArrayList<AreaGestion>();
        String sql = "SELECT codArea,nomArea,estArea FROM AreaGestion ORDER BY nomArea";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
             area = new AreaGestion();
            area.setId(rs.getInt("codArea"));
            area.setNombre(rs.getString("nomArea"));
            area.setEstado(rs.getInt("estArea"));
            lista.add(area);
        }
        return lista;
    }
    
    public boolean addCorreo(int key, String correo) throws SQLException {
        String sql = "INSERT INTO CorreoAreaGestion (codArea,correo) VALUES (?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        pst.setString(2, correo);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }
    public boolean removeCorreo(int key) throws SQLException {
        String sql = "DELETE FROM CorreoAreaGestion WHERE idCorreo=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }
}
