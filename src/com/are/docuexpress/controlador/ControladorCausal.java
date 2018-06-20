/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Causal;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorCausal {
    private db conexion;
    private Causal causal;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Causal getCausal() {
        return causal;
    }

    public void setCausal(Causal causal) {
        this.causal = causal;
    }

    public ControladorCausal(db conexion) {
        this.conexion = conexion;
    }

    public ControladorCausal(db conexion, Causal causal) {
        this.conexion = conexion;
        this.causal = causal;
    }
    
    public boolean add(Causal causal) throws SQLException {
        boolean result=false;
        String sql = String.format("INSERT INTO causal (codCausal, nomCausal,estCausal) VALUES ('%s','%s','%s')", causal.getCodigo(), causal.getNombre(), causal.getEstado());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean update(Causal causal, String key) throws SQLException {
        boolean result=false;
        String sql = String.format("UPDATE causal SET codCausal='%s', nomCausal='%s', estCausal='%s' WHERE codCausal='%s'", causal.getCodigo(), causal.getNombre(),causal.getEstado(), key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean remove(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("DELETE causal WHERE codCausal='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT codCausal,nomCausal,estCausal FROM causal WHERE codCausal='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            causal = new Causal();
            causal.setCodigo(rs.getString("codCausal"));
            causal.setNombre(rs.getString("nomCausal"));
            causal.setEstado(rs.getInt("estCausal"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<Causal> list() throws SQLException {
        ArrayList<Causal> lista = new ArrayList<Causal>();
        String sql = "SELECT codCausal,nomCausal,estCausal FROM causal ORDER BY nomCausal";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Causal causal = new Causal();
            causal.setCodigo(rs.getString("codCausal"));
            causal.setNombre(rs.getString("nomCausal"));
            causal.setEstado(rs.getInt("estCausal"));
            
            lista.add(causal);
        }
        
        return lista;
    }
    
}
