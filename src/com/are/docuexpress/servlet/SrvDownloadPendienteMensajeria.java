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
@WebServlet("/SrvDownloadPendienteMensajeria")
public class SrvDownloadPendienteMensajeria extends HttpServlet {

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
            
        String oficina = (String)request.getParameter("oficina");
        String fecha1 = (String)request.getParameter("fecha1");
        String fecha2 = (String)request.getParameter("fecha2");
        db conexion = null;

        try {

            conexion = new db();
            
            String sql = "SELECT documentos.*, oficina.nomOficina,departamento.nomDepartamento,empresa.nomEmpresa,Delegacion.nomDelegacion,archivos.fecha "
                    + "FROM documentos, archivos,oficina,departamento,delegacion,empresa "
                    + "WHERE documentos.idCarga= archivos.id "
                    + "AND archivos.oficina = oficina.codOficina "
                    + "AND oficina.idDepartamento = departamento.codDepartamento "
                    + "AND oficina.idDelegacion = delegacion.codDelegacion "
                    + "AND archivos.empresa = empresa.codEmpresa "
                    + "AND (dbo.ContadorGuiasDocumento(documentos.id) = 0 OR dbo.ContadorImagenGuiasDocumento(documentos.id) = 0 )";
            if (!oficina.equals("all")) {
                sql += " AND oficina.codOficina = '" + oficina + "'";
            }
            
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            
            java.sql.ResultSet rs = conexion.Query(pst);
    
            out.println("NIC" + separador 
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
                    + "IDENTIFICADOR" + separador
                    + "FECHA CARGA");

            while (rs.next()) {
                String fila = CleanStr(rs.getString("nic")) + separador;
                fila += CleanStr(rs.getString("nroDocumento")) + separador;
                fila += CleanStr(rs.getString("nomDocumento")) + separador;
                fila += CleanStr(rs.getString("municipio")) + separador;
                fila += CleanStr(rs.getString("barrio")) + separador;
                fila += CleanStr(rs.getString("via")) + separador;
                fila += CleanStr(rs.getString("crucero")) + separador;
                fila += CleanStr(rs.getString("placa")) + separador;
                fila += CleanStr(rs.getString("nomReclamante")) + separador;
                fila += CleanStr(rs.getString("telefono")) + separador;
                fila += CleanStr(rs.getString("nomOficina")) + separador;
                fila += CleanStr(rs.getString("nomDepartamento")) + separador;
                fila += CleanStr(rs.getString("nomDelegacion")) + separador;
                fila += CleanStr(rs.getString("nomEmpresa")) + separador;
                fila += CleanStr(rs.getString("numProceso")) + separador;
                fila += CleanStr(rs.getString("id")) + separador;
                fila += CleanStr(rs.getString("fecha"));
                out.println(fila);
            }

        }catch (SQLException e) {
            out.println("Error: " + e.getMessage());
        }finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    out.println("Error: " + ex.getMessage());
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

    private String CleanStr(String str) {
        String cadena = str.replace("\r", "");
        cadena = cadena.replace("\n", "");
        cadena = cadena.replace("\t", "");
        cadena = cadena.replace(";", "");
        
        return cadena;
    }
}
