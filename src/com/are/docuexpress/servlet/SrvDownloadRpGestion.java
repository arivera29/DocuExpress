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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Aimer
 */
@WebServlet("/SrvDownloadRpGestion")
public class SrvDownloadRpGestion extends HttpServlet {

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
        String headerValue = String.format("attachment; filename=\"%s\"", "ReporteDeGestion.csv");
        String separador = ";";
        response.setHeader(headerKey, headerValue);
            
        String oficina = (String)request.getParameter("oficina");
        String delegacion = (String)request.getParameter("delegacion");
        String departamento = (String)request.getParameter("departamento");
        String tipo = (String)request.getParameter("tipo");
        String fecha1 = (String)request.getParameter("fecha1");
        String fecha2 = (String)request.getParameter("fecha2");
        
        
        
        db conexion = null;

        try {

            conexion = new db();
            String sql = "SELECT documentos.ID AS IDENTIFICADOR, "
                    + "oficina.nomOficina AS OFICINA,"
                    + "guias.numeroGuia AS NUMERO_GUIA,"
                    + "documentos.nroDocumento AS NUMERO_DOCUMENTO,"
                    + " documentos.nic AS NIC, "
                    + "tipoDocumento.nomTipoDocumento AS NOMBRE_DOCUMENTO,"
                    + "documentos.numProceso as PROCESO, "
                    + "documentos.codDocumento as CODIGO_DOCUMENTO, "
                    + "documentos.nomReclamante AS NOMBRE_RECLAMANTE,"
                    + "departamento.nomDepartamento AS DEPARTAMENTO,"
                    + "Delegacion.nomDelegacion AS DELEGACION, "
                    + "documentos.municipio AS MUNICIPIO,"
                    + "documentos.barrio AS BARRIO,"
                    + "documentos.via AS VIA,"
                    + "documentos.crucero AS CRUCERO,"
                    + "documentos.placa AS PLACA,"
                    + "'' AS DESTINO,"
                    + "CASE guias.estadoGuia WHEN 2 THEN 'ENTREGADO' WHEN 3 THEN 'DEVUELTO' ELSE 'DESCONOCIDO' END AS ESTADO,"
                    + "causal.nomCausal CAUSAL_DEVOLUCION,"
                    + "CASE guias.UploadImagen WHEN 1 THEN 'SI' ELSE 'NO' END AS IMAGEN, "
                    + "CONVERT(DATE,archivos.fecha) as FECHA_CARGA, "
                    + "CONVERT(TIME,archivos.fecha) as HORA_CARGA, "
                    + "archivos.usuario as USUARIO_CARGA, "
                    + "CONVERT(DATE,archivos.fecha_impresion) as FECHA_IMPRESION, "
                    + "CONVERT(TIME,archivos.fecha_impresion) as HORA_IMPRESION, "
                    + "archivos.user_impresion as USUARIO_IMPRESION, "
                    + "DATEDIFF(day,archivos.fecha,archivos.fecha_impresion) AS TIEMPO_IMPRESION, "
                    + "CONVERT(DATE,guias.fechaGestion) as FECHA_GESTION, "
                    + "DATEDIFF(day,archivos.fecha,guias.fechaGestion) AS TIEMPO_GESTION, "
                    + "CONVERT(DATE,guias.fechaSistemaGestion) as FECHA_ESTADO, "
                    + "DATEDIFF(day,archivos.fecha,guias.fechaSistemaGestion) AS TIEMPO_ESTADO, "
                    + "CONVERT(DATE,guias.fechaUploadImagen) as FECHA_IMAGEN, "
                    + "DATEDIFF(day,archivos.fecha,guias.fechaUploadImagen) AS TIEMPO_ESTADO, "
                    + "CONVERT(DATE,guias.fechaCarga) as FECHA_GENERACION_GUIA, "
                    + "DATEDIFF(day,archivos.fecha,guias.fechaCarga) AS TIEMPO_GENERACION_GUIA "
                    + "FROM documentos "
                    + "LEFT JOIN guias ON documentos.id = guias.idDocumento "
                    + "LEFT JOIN causal ON guias.causalDevolucion = causal.codCausal , "
                    + "archivos,oficina,departamento,delegacion,tipoDocumento "
                    + "WHERE documentos.idCarga= archivos.id "
                    + "AND archivos.oficina = oficina.codOficina "
                    + "AND oficina.idDepartamento = departamento.codDepartamento "
                    + "AND oficina.idDelegacion = delegacion.codDelegacion "
                    + "AND documentos.nomDocumento = tipoDocumento.codTipoDocumento "
                    + "AND CONVERT(date,archivos.fecha) BETWEEN  CONVERT(date,?) AND CONVERT(date,?) ";
            
            if (!oficina.equals("all")) {
               sql += "AND archivos.oficina = '" + oficina + "' "; 
            }
            
            if (!delegacion.equals("all")) {
                sql += "AND oficina.idDelegacion = '" + delegacion + "' ";
            }
            
            if (!departamento.equals("all")) {
                sql += "AND oficina.idDepartamento = '" + departamento + "' ";
            }
            
            if (!tipo.equals("all")) {
                sql += "AND documentos.nomDocumento = '" + tipo + "' ";
            }
                    
            sql += "ORDER BY archivos.fecha";
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
                    fila += parseString(rs.getString(x)) + separador;
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

    private String parseString(String cadena) {
        if (cadena == null) cadena="NULL";
        cadena = cadena.replace("\t", "");
        cadena = cadena.replace("\n", "");
        cadena = cadena.replace("\r", "");
        cadena = cadena.replace(";", "");
        
        return cadena;
    }
}
