/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Oficina;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorOficina {
    
    private db conexion=null;
    private Oficina oficina;

    public ControladorOficina(db conexion) {
        this.conexion = conexion;
    }

    public ControladorOficina(db conexion, Oficina oficina) {
        this.conexion = conexion;
        this.oficina = oficina;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }
    
    
    
    public boolean add(Oficina oficina) throws SQLException {
        String sql = String.format("INSERT INTO oficina (codOficina, nomOficina, dirOficina,estOficina,idDepartamento,idDelegacion) VALUES ('%s','%s','%s','%s','%s','%s')", oficina.getCodigo(),oficina.getNombre(), oficina.getDireccion(), oficina.getEstado(),oficina.getDepartamento(),oficina.getDelegacion());
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        
        return false;
    }
    
    public boolean update(Oficina oficina, String key) throws SQLException {
        String sql = String.format("UPDATE oficina SET codOficina='%s', nomOficina='%s', dirOficina='%s',estOficina='%s',idDepartamento='%s', idDelegacion='%s' WHERE codOficina='%s'", oficina.getCodigo(),oficina.getNombre(), oficina.getDireccion(), oficina.getEstado(),oficina.getDepartamento(),oficina.getDelegacion(), key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }
        return false;
    }
    
    public boolean remove(String key) throws SQLException {
        String sql = String.format("DELETE FROM oficina WHERE codOficina='%s'",key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            return true;
        }

        
        return false;
    }
    
    public boolean find(String key) throws SQLException {
        String sql = String.format("SELECT codOficina,nomOficina,dirOficina,estOficina,idDepartamento,idDelegacion FROM oficina WHERE codOficina='%s'", key);
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            oficina = new Oficina();
            oficina.setCodigo(rs.getString("codOficina"));
            oficina.setNombre(rs.getString("nomOficina"));
            oficina.setDireccion(rs.getString("dirOficina"));
            oficina.setEstado(rs.getInt("estOficina"));
            oficina.setDepartamento(rs.getString("idDepartamento"));
            oficina.setDelegacion(rs.getString("idDelegacion"));
            return true;
        }

        
        return false;
    }
    
    public ArrayList<Oficina> list() throws SQLException {
        ArrayList<Oficina> lista = new ArrayList<Oficina>();
        String sql = "SELECT codOficina,nomOficina,dirOficina,estOficina,idDepartamento,idDelegacion FROM oficina ORDER BY nomOficina";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Oficina oficina = new Oficina();
            oficina.setCodigo(rs.getString("codOficina"));
            oficina.setNombre(rs.getString("nomOficina"));
            oficina.setDireccion(rs.getString("dirOficina"));
            oficina.setEstado(rs.getInt("estOficina"));
            oficina.setDepartamento(rs.getString("idDepartamento"));
            oficina.setDelegacion(rs.getString("idDelegacion"));
            lista.add(oficina);
        }
        return lista;
    }
    
}
