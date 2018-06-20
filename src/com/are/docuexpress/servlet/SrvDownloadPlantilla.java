/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Aimer
 */
@WebServlet("/SrvDownloadPlantilla")
public class SrvDownloadPlantilla extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "plantilla.csv");
        String separador = ";";
        response.setHeader(headerKey, headerValue);
            
        String id = (String)request.getParameter("id");
        db conexion = null;

        try {

            conexion = new db();
            String sql = "SELECT documentos.*, convert(date,archivos.fecha) as fecha, "
                    + "oficina.nomOficina,departamento.nomDepartamento,empresa.nomEmpresa,Delegacion.nomDelegacion, TipoDocumento.nomTipoDocumento "
                    + "FROM documentos, archivos,oficina,departamento,delegacion,empresa,TipoDocumento "
                    + "WHERE documentos.idCarga= archivos.id "
                    + "AND archivos.oficina = oficina.codOficina "
                    + "AND documentos.nomDocumento = TipoDocumento.codTipoDocumento "
                    + "AND oficina.idDepartamento = departamento.codDepartamento "
                    + "AND oficina.idDelegacion = delegacion.codDelegacion "
                    + "AND archivos.empresa = empresa.codEmpresa "
                    + "AND idCarga = ? ORDER BY id";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, id);
            java.sql.ResultSet rs = conexion.Query(pst);
    
            out.println("FECHA" +  separador
                    + "NIC" + separador 
                    + "NRO. DOCUMENTO" + separador 
                    + "NOMBRE DOCUMENTO"+ separador 
                    + "MUNCIPIO" + separador 
                    + "BARRIO"+ separador 
                    + "VIA" + separador 
                    + "CRUCERO" + separador 
                    + "PLACA" + separador 
                    + "NOMBRE RECLAMANTE" + separador 
                    + "TELEFONO" + separador
                    + "OFICINA" + separador
                    + "DEPARTAMETO" + separador
                    + "DELEGACION" + separador
                    + "EMPRESA" + separador
                    + "PROCESO" + separador
                    + "IDENTIFICADOR" );

            while (rs.next()) {
                String fila = parseString(rs.getString("fecha")) + separador;
                fila += parseString(rs.getString("nic")) + separador;
                fila += parseString(rs.getString("nroDocumento")) + separador;
                fila += parseString(rs.getString("nomTipoDocumento")) + separador;
                fila += parseString(rs.getString("municipio")) + separador;
                fila += parseString(rs.getString("barrio")) + separador;
                fila += parseString(rs.getString("via")) + separador;
                fila += parseString(rs.getString("crucero")) + separador;
                fila += parseString(rs.getString("placa")) + separador;
                fila += parseString(rs.getString("nomReclamante")) + separador;
                fila += parseString(rs.getString("telefono")) + separador;
                fila += parseString(rs.getString("nomOficina")) + separador;
                fila += parseString(rs.getString("nomDepartamento")) + separador;
                fila += parseString(rs.getString("nomDelegacion")) + separador;
                fila += parseString(rs.getString("nomEmpresa")) + separador;
                fila += parseString(rs.getString("numProceso")) + separador;
                fila += parseString(rs.getString("id"));
                out.println(fila);
            }

        }catch (SQLException e) {
            out.println("Error: " + e.getMessage());
        }finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(SrvDownloadPlantilla.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String parseString(String cadena) {
        if (cadena == null) cadena="NULL";
        cadena = cadena.replace("\t", "");
        cadena = cadena.replace("\n", "");
        cadena = cadena.replace("\r", "");
        cadena = cadena.replace(";", "");
        
        return cadena;
    }
}
