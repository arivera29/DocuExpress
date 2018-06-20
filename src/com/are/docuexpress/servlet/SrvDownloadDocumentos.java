/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
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
@WebServlet("/SrvDownloadDocumentos")
public class SrvDownloadDocumentos extends HttpServlet {

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
        String empresa = (String)request.getParameter("empresa");
        
        
        db conexion = null;

        try {

            conexion = new db();
            String sql = "SELECT documentos.nic AS NIC, "
                    + "tipoDocumento.nomTipoDocumento AS NOMBRE_DOCUMENTO,"
                    + "documentos.nroDocumento AS NUMERO_DOCUMENTO,"
                    + "documentos.municipio AS MUNICIPIO,"
                    + "documentos.barrio AS BARRIO,"
                    + "documentos.via AS VIA,"
                    + "documentos.crucero AS CRUCERO,"
                    + "documentos.placa AS PLACA,"
                    + "documentos.nomReclamante AS NOMBRE_RECLAMANTE,"
                    + "documentos.telefono AS TELEFONO,"
                    + "oficina.nomOficina AS OFICINA,"
                    + "departamento.nomDepartamento AS DEPARTAMENTO,"
                    + "Delegacion.nomDelegacion AS DELEGACION, "
                    + "empresa.nomEmpresa EMPRESA, "
                    + "documentos.numProceso as PROCESO, "
                    + "documentos.codDocumento as CODIGO_DOCUMENTO, "
                    + "documentos.id as IDENTIFICADOR, "
                    + "archivos.fecha as FECHA_CARGA, "
                    + "archivos.usuario as USUARIO_CARGA "
                    + "FROM documentos, archivos,oficina,departamento,delegacion,empresa,tipoDocumento "
                    + "WHERE documentos.idCarga= archivos.id "
                    + "AND archivos.oficina = oficina.codOficina "
                    + "AND oficina.idDepartamento = departamento.codDepartamento "
                    + "AND oficina.idDelegacion = delegacion.codDelegacion "
                    + "AND archivos.empresa = empresa.codEmpresa "
                    + "AND documentos.nomDocumento = tipoDocumento.codTipoDocumento "
                    + "AND CONVERT(date,fechaCarga) BETWEEN  CONVERT(date,?) AND CONVERT(date,?) ";
            
            if (!oficina.equals("all")) {
               sql += "AND archivos.oficina = '" + oficina + "' "; 
            }
            
            if (!empresa.equals("all")) {
                sql += "AND archivos.empresa = '" + empresa + "' ";
            }
                    
            sql += "ORDER BY fechaCarga";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, fecha1);
            pst.setString(2, fecha2);
            java.sql.ResultSet rs = conexion.Query(pst);
    
           ResultSetMetaData rsmd = rs.getMetaData();
           String fila="";
           for (int x=1; x <= rsmd.getColumnCount(); x++) {
                fila += rsmd.getColumnLabel(x) + separador;
                
           }
           out.println(fila);
           
            while (rs.next()) {
                fila="";
                for (int x=1; x <= rsmd.getColumnCount(); x++) {
                    fila += rs.getString(x) + separador;
                }
                out.println(fila);
            }

        }catch (SQLException e) {
            out.println("Error: " + e.getMessage());
        }finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(SrvDownloadDocumentos.class.getName()).log(Level.SEVERE, null, ex);
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

}
