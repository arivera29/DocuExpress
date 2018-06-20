/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Archivo;
import java.sql.SQLException;

/**
 *
 * @author Aimer
 */
public class ControladorArchivo {
    private db conexion;
    private Archivo archivo;
    private int lastIdCreated=-1;

    public ControladorArchivo(db conexion) {
        this.conexion = conexion;
    }

    public ControladorArchivo(db conexion, Archivo archivo) {
        this.conexion = conexion;
        this.archivo = archivo;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }
    
    public boolean add(Archivo archivo) throws SQLException {
        this.lastIdCreated = -1;
        String sql = "INSERT INTO archivos (filename,size,oficina,empresa,usuario,fecha,estado) VALUES (?,?,?,?,?,SYSDATETIME(),1)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, archivo.getFilename());
        pst.setLong(2, archivo.getSize());
        pst.setString(3, archivo.getOficina());
        pst.setString(4, archivo.getEmpresa());
        pst.setString(5, archivo.getUsuario());
        
        if (conexion.Update(pst) >0 ) {
            this.lastIdCreated = this.getLastIdCreated();
            return true;
        }
        return false;
    }
    
    public boolean find(int key) throws SQLException {
        String sql = "SELECT * FROM archivos WHERE id=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            archivo = new Archivo();
            archivo.setId(rs.getInt("id"));
            archivo.setFilename(rs.getString("filename"));
            archivo.setSize(rs.getLong("size"));
            archivo.setOficina(rs.getString("oficina"));
            archivo.setEmpresa(rs.getString("empresa"));
            archivo.setUsuario(rs.getString("usuario"));
            archivo.setFecha(rs.getString("fecha"));
            return true;
        }
        
        
        return false;
    }
    
    public int getLastIdCreated() throws SQLException {
        int id=-1;
        String sql = "SELECT @@IDENTITY AS 'Identity'";
        java.sql.ResultSet rs = conexion.Query(sql);
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }
}
