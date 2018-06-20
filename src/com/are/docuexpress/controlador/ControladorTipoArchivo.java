/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.TipoArchivo;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorTipoArchivo {
    db conexion;
    TipoArchivo tipo;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public TipoArchivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoArchivo tipo) {
        this.tipo = tipo;
    }

    public ControladorTipoArchivo(db conexion) {
        this.conexion = conexion;
    }

    public ControladorTipoArchivo(db conexion, TipoArchivo tipo) {
        this.conexion = conexion;
        this.tipo = tipo;
    }
    
    public boolean add(TipoArchivo tipo) throws SQLException {
        boolean result=false;
        String sql = String.format("INSERT INTO TipoArchivo (codTipoArchivo, nomTipoArchivo,estTipoArchivo, ordTipoArchivo) VALUES ('%s','%s','%s',%s)", tipo.getCodigo(), tipo.getNombre(), tipo.getEstado(), tipo.getOrden());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean update(TipoArchivo tipo, String key) throws SQLException {
        boolean result=false;
        String sql = String.format("UPDATE TipoArchivo SET codTipoArchivo='%s', nomTipoArchivo='%s', estTipoArchivo='%s', ordTipoArchivo=%s WHERE codTipoArchivo='%s'", tipo.getCodigo(), tipo.getNombre(),tipo.getEstado(), tipo.getOrden(), key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean remove(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("DELETE TipoArchivo WHERE codTipoArchivo='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT codTipoArchivo,nomTipoArchivo,estTipoArchivo, ordTipoArchivo FROM TipoArchivo WHERE codTipoArchivo='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            tipo = new TipoArchivo();
            tipo.setCodigo(rs.getString("codTipoArchivo"));
            tipo.setNombre(rs.getString("nomTipoArchivo"));
            tipo.setEstado(rs.getInt("estTipoArchivo"));
            tipo.setOrden(rs.getInt("ordTipoArchivo"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<TipoArchivo> list() throws SQLException {
        ArrayList<TipoArchivo> lista = new ArrayList<TipoArchivo>();
        String sql = "SELECT codTipoArchivo,nomTipoArchivo,estTipoArchivo, ordTipoArchivo FROM TipoArchivo ORDER BY ordTipoArchivo";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            TipoArchivo tipo = new TipoArchivo();
            tipo.setCodigo(rs.getString("codTipoArchivo"));
            tipo.setNombre(rs.getString("nomTipoArchivo"));
            tipo.setEstado(rs.getInt("estTipoArchivo"));
            tipo.setOrden(rs.getInt("ordTipoArchivo"));
            
            lista.add(tipo);
        }
        
        return lista;
    }
    
}
