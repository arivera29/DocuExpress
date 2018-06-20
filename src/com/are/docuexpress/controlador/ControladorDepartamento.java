/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Departamento;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorDepartamento {
    private db conexion = null;
    private Departamento departamento;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    
    public ControladorDepartamento(db conexion) {
        this.conexion = conexion;
    }

    public ControladorDepartamento(db conexion, Departamento departamento) {
        this.conexion = conexion;
        this.departamento = departamento;
    }
    
    public boolean add(Departamento departamento) throws SQLException {
        String sql = String.format("INSERT INTO departamento (codDepartamento, nomDepartamento) VALUES ('%s','%s')", departamento.getCodigo(),departamento.getNombre());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }
    
    public boolean update(Departamento departamento, String key) throws SQLException {
        String sql = String.format("UPDATE departamento SET codDepartamento='%s', nomDepartamento='%s' WHERE codDepartamento='%s'", departamento.getCodigo(),departamento.getNombre(),key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }

    public boolean remove(String key) throws SQLException {
        String sql = String.format("DELETE FROM departamento WHERE codDepartamento='%s'",key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = String.format("SELECT codDepartamento,nomDepartamento FROM departamento WHERE codDepartamento='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            departamento = new Departamento();
            departamento.setCodigo(rs.getString("codDepartamento"));
            departamento.setNombre(rs.getString("nomDepartamento"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<Departamento> list() throws SQLException {
        ArrayList<Departamento> lista = new ArrayList<Departamento>();
        String sql = "SELECT codDepartamento,nomDepartamento FROM departamento ORDER BY nomDepartamento";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Departamento departamento = new Departamento();
            departamento.setCodigo(rs.getString("codDepartamento"));
            departamento.setNombre(rs.getString("nomDepartamento"));
            
            lista.add(departamento);
        }
        
        
        return lista;
    }
}
