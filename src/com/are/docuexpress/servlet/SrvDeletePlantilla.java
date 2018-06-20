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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Aimer
 */
@WebServlet("/SrvDeletePlantilla")
public class SrvDeletePlantilla extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String usuario = (String) session.getAttribute("usuario");
        String perfil = (String) session.getAttribute("perfil");
        String id = (String) request.getParameter("id");
        try (PrintWriter out = response.getWriter()) {
           
            db conexion = null;
            try {
                conexion = new db();
                
                String sql = "SELECT archivos.*, DATEDIFF(minute,fecha,SYSDATETIME()) tiempo "
                        + " FROM archivos WHERE id=?";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, id);
                java.sql.ResultSet rs = conexion.Query(pst);
                if (rs.next()) {
                    
                    if (rs.getInt("tiempo") > 15 && !perfil.equals("1")) {
                        throw new Exception("No se puede eliminar la plantilla, excede el tiempo de revisión");
                    }
                    
                    if (rs.getString("usuario").equals(usuario) || usuario.toUpperCase().equals("ADMIN") || perfil.equals("1")) {
                        sql = String.format("DELETE FROM archivo_impresion WHERE id_archivo= %s ",id);
                        if (conexion.Update(sql) >= 0) {
                            sql = String.format("DELETE FROM documentos WHERE idCarga= %s ",id);
                            if (conexion.Update(sql) >= 0) {
                                sql = String.format("UPDATE archivos SET estado=0 WHERE id = %s ",id);
                                if (conexion.Update(sql) >= 0) {
                                    conexion.Commit();
                                    out.print("OK");
                                    
                                }else {
                                    conexion.Rollback();
                                    out.print("Error al eliminar la plantilla");
                                }
                                
                            }else {
                                conexion.Rollback();
                                out.print("Error al eliminar los registros de la plantilla");
                            }
                            
                            
                        }else {
                            conexion.Rollback();
                            out.print("Error al eliminar los archivos adjuntos a la plantilla");
                        }
                        
                        
                    }else {
                        out.print("No tiene permisos para eliminar la plantilla");
                    }
                }else {
                    out.print("ID de archivo no encontrado, verifique por favor");
                }
                

            } catch (SQLException e) {
                out.print("Error. " + e.getMessage());
            } catch (Exception ex) {
                out.print("Error. " + ex.getMessage());
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        out.print("Error. " + ex.getMessage());
                    }
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
