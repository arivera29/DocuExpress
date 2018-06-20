/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.IndicadorMensajeria;
import java.sql.SQLException;

/**
 *
 * @author Aimer
 */
public class ControladorIndicadorMensajeria {
    private db conex;
    private IndicadorMensajeria indicador;

    public ControladorIndicadorMensajeria(db conex) {
        this.conex = conex;
    }

    public ControladorIndicadorMensajeria(db conex, IndicadorMensajeria indicador) {
        this.conex = conex;
        this.indicador = indicador;
    }

    public db getConex() {
        return conex;
    }

    public void setConex(db conex) {
        this.conex = conex;
    }

    public IndicadorMensajeria getIndicador() {
        return indicador;
    }

    public void setIndicador(IndicadorMensajeria indicador) {
        this.indicador = indicador;
    }
    
    public void Calcular(java.util.Date fecha) throws SQLException {
        this.indicador = new IndicadorMensajeria();
        String sql = "SELECT SUM(CASE WHEN tipo='PCG' THEN 1 ELSE 0 END) AS PCG, "
                + "SUM(CASE WHEN tipo='PG' THEN 1 ELSE 0 END) AS PG, "
                + "SUM(CASE WHEN tipo='imagen' THEN 1 ELSE 0 END) AS UPLOAD, "
                + "SUM(CASE WHEN tipo='entrega' THEN 1 ELSE 0 END) AS ENTREGA, "
                + "SUM(CASE WHEN tipo='devolucion' THEN 1 ELSE 0 END) AS DEVOLUCION, "
                + "SUM(CASE WHEN tipo='guia' THEN 1 ELSE 0 END) AS GUIA "
                + "FROM AuditoriaMensajeria "
                + "WHERE CONVERT(DATE,fecha) = CONVERT(DATE, ?)";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setDate(1, new java.sql.Date(fecha.getTime()));
        java.sql.ResultSet rs = conex.Query(pst);
        if (rs.next()) {
            indicador.setPeticionPCG(rs.getInt("PCG"));
            indicador.setPeticionPG(rs.getInt("PG"));
            indicador.setPeticionUploadImagen(rs.getInt("UPLOAD"));
            indicador.setPeticionEntrega(rs.getInt("ENTREGA"));
            indicador.setPeticionDevolucion(rs.getInt("DEVOLUCION"));
            indicador.setPeticionGuia(rs.getInt("GUIA"));
        }
        
        
    }
    
}
