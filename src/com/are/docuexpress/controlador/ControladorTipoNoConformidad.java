/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.TipoNoConformidad;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aimer
 */
public class ControladorTipoNoConformidad {
    db conexion;
    TipoNoConformidad tipo;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public TipoNoConformidad getTipo() {
        return tipo;
    }

    public void setTipo(TipoNoConformidad tipo) {
        this.tipo = tipo;
    }

    public ControladorTipoNoConformidad(db conexion) {
        this.conexion = conexion;
    }

    public ControladorTipoNoConformidad(db conexion, TipoNoConformidad tipo) {
        this.conexion = conexion;
        this.tipo = tipo;
    }
    
    public boolean add(TipoNoConformidad tipo) throws SQLException {
        boolean result=false;
        String sql = "INSERT INTO TipoNoConformidad (codTipoNoConformidad, nomTipoNoConformidad,"
                + "estTipoNoConformidad,correoNotificacion1, correoNotificacion2, nivelCriticidad, responsable) "
                + "VALUES (?,?,?,?,?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, tipo.getCodigo());
        pst.setString(2, tipo.getNombre());
        pst.setInt(3, tipo.getEstado());
        pst.setString(4, tipo.getCorreoNotificacion1());
        pst.setString(5, tipo.getCorreoNotificacion2());
        pst.setInt(6, tipo.getNivelCriticidad());
        pst.setInt(7, tipo.getResponsable());
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean update(TipoNoConformidad tipo, String key) throws SQLException {
        boolean result=false;
        String sql = "UPDATE TipoNoConformidad SET codTipoNoConformidad=?, nomTipoNoConformidad=?, estTipoNoConformidad=?, "
                + " correoNotificacion1=?, correoNotificacion2=?, nivelCriticidad=?,responsable=?  WHERE codTipoNoConformidad=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, tipo.getCodigo());
        pst.setString(2, tipo.getNombre());
        pst.setInt(3, tipo.getEstado());
        pst.setString(4, tipo.getCorreoNotificacion1());
        pst.setString(5, tipo.getCorreoNotificacion2());
        pst.setInt(6, tipo.getNivelCriticidad());
        pst.setInt(7, tipo.getResponsable());
        pst.setString(8, key);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean remove(String key) throws SQLException {
        boolean result=false;
        String sql = "DELETE TipoNoConformidad WHERE codTipoNoConformidad = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, key);
        if (conexion.Update(pst)>0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean find(String key) throws SQLException {
        boolean result=false;
        String sql = "SELECT codTipoNoConformidad,nomTipoNoConformidad,estTipoNoConformidad,"
                + "correoNotificacion1, correoNotificacion2,nivelCriticidad,responsable "
                + "FROM TipoNoConformidad WHERE codTipoNoConformidad = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            tipo = new TipoNoConformidad();
            tipo.setCodigo(rs.getString("codTipoNoConformidad"));
            tipo.setNombre(rs.getString("nomTipoNoConformidad"));
            tipo.setEstado(rs.getInt("estTipoNoConformidad"));
            tipo.setCorreoNotificacion1(rs.getString("correoNotificacion1"));
            tipo.setCorreoNotificacion2(rs.getString("correoNotificacion2"));
            tipo.setNivelCriticidad(rs.getInt("nivelCriticidad"));
            tipo.setResponsable(rs.getInt("responsable"));
            
            result = true;
        }
        
        
        return result;
    }
    
    public ArrayList<TipoNoConformidad> list() throws SQLException {
        ArrayList<TipoNoConformidad> lista = new ArrayList<TipoNoConformidad>();
        String sql = "SELECT codTipoNoConformidad,nomTipoNoConformidad,estTipoNoConformidad,"
                + "correoNotificacion1, correoNotificacion2,nivelCriticidad,responsable "
                + "FROM TipoNoConformidad ORDER BY nomTipoNoConformidad ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            TipoNoConformidad tipo = new TipoNoConformidad();
            tipo.setCodigo(rs.getString("codTipoNoConformidad"));
            tipo.setNombre(rs.getString("nomTipoNoConformidad"));
            tipo.setEstado(rs.getInt("estTipoNoConformidad"));
            tipo.setCorreoNotificacion1(rs.getString("correoNotificacion1"));
            tipo.setCorreoNotificacion2(rs.getString("correoNotificacion2"));
            tipo.setNivelCriticidad(rs.getInt("nivelCriticidad"));
            tipo.setResponsable(rs.getInt("responsable"));
            lista.add(tipo);
        }
        
        return lista;
    }
    
    public ArrayList<TipoNoConformidad> list(int responsable) throws SQLException {
        ArrayList<TipoNoConformidad> lista = new ArrayList<TipoNoConformidad>();
        String sql = "SELECT codTipoNoConformidad,nomTipoNoConformidad,estTipoNoConformidad,"
                + "correoNotificacion1, correoNotificacion2,nivelCriticidad,responsable "
                + "FROM TipoNoConformidad "
                + "WHERE responsable = ?"
                + "ORDER BY nomTipoNoConformidad ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, responsable);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            TipoNoConformidad tipo = new TipoNoConformidad();
            tipo.setCodigo(rs.getString("codTipoNoConformidad"));
            tipo.setNombre(rs.getString("nomTipoNoConformidad"));
            tipo.setEstado(rs.getInt("estTipoNoConformidad"));
            tipo.setCorreoNotificacion1(rs.getString("correoNotificacion1"));
            tipo.setCorreoNotificacion2(rs.getString("correoNotificacion2"));
            tipo.setNivelCriticidad(rs.getInt("nivelCriticidad"));
            tipo.setResponsable(rs.getInt("responsable"));
            lista.add(tipo);
        }
        
        return lista;
    }
    
}
