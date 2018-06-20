/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.TipoDocumento;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorTipoDocumento {
    db conexion;
    TipoDocumento tipo;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocumento tipo) {
        this.tipo = tipo;
    }

    public ControladorTipoDocumento(db conexion) {
        this.conexion = conexion;
    }

    public ControladorTipoDocumento(db conexion, TipoDocumento tipo) {
        this.conexion = conexion;
        this.tipo = tipo;
    }
    
    public boolean add(TipoDocumento tipo) throws SQLException {
        boolean result=false;
        String sql = String.format("INSERT INTO TipoDocumento (codTipoDocumento, nomTipoDocumento,estTipoDocumento) VALUES ('%s','%s','%s')", tipo.getCodigo(), tipo.getNombre(), tipo.getEstado());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean update(TipoDocumento tipo, String key) throws SQLException {
        boolean result=false;
        String sql = String.format("UPDATE TipoDocumento SET codTipoDocumento='%s', nomTipoDocumento='%s', estTipoDocumento='%s' WHERE codTipoDocumento='%s'", tipo.getCodigo(), tipo.getNombre(),tipo.getEstado(), key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean remove(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("DELETE TipoDocumento WHERE codTipoDocumento='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT codTipoDocumento,nomTipoDocumento,estTipoDocumento FROM TipoDocumento WHERE codTipoDocumento='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            tipo = new TipoDocumento();
            tipo.setCodigo(rs.getString("codTipoDocumento"));
            tipo.setNombre(rs.getString("nomTipoDocumento"));
            tipo.setEstado(rs.getInt("estTipoDocumento"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public boolean findByName(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT codTipoDocumento,nomTipoDocumento,estTipoDocumento FROM TipoDocumento WHERE nomTipoDocumento='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            tipo = new TipoDocumento();
            tipo.setCodigo(rs.getString("codTipoDocumento"));
            tipo.setNombre(rs.getString("nomTipoDocumento"));
            tipo.setEstado(rs.getInt("estTipoDocumento"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<TipoDocumento> list() throws SQLException {
        ArrayList<TipoDocumento> lista = new ArrayList<TipoDocumento>();
        String sql = "SELECT codTipoDocumento,nomTipoDocumento,estTipoDocumento FROM TipoDocumento ORDER BY nomTipoDocumento";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            TipoDocumento tipo = new TipoDocumento();
            tipo.setCodigo(rs.getString("codTipoDocumento"));
            tipo.setNombre(rs.getString("nomTipoDocumento"));
            tipo.setEstado(rs.getInt("estTipoDocumento"));
            
            lista.add(tipo);
        }
        
        return lista;
    }
    
}
