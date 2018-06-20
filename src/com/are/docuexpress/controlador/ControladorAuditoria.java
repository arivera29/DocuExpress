/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import java.sql.SQLException;

/**
 *
 * @author Aimer
 */
public class ControladorAuditoria {
    private db conex;

    public ControladorAuditoria(db conex) {
        this.conex = conex;
    }
    
    
    public void Mensajeria(String expediente, String json, String tipo, String usuario) throws SQLException {
        String sql = "INSERT INTO AuditoriaMensajeria (expediente, json,tipo,usuario,fecha) VALUES (?,?,?,?,SYSDATETIME())";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, expediente);
        pst.setString(2, json);
        pst.setString(3, tipo);
        pst.setString(4, usuario);
        if (conex.Update(pst) > 0) {
            conex.Commit();
        }
    }
    
}
