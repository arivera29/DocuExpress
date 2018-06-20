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
@WebServlet("/SrvDeleteFile")
public class SrvDeleteFile extends HttpServlet {

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
                if (usuario.toUpperCase().equals("ADMIN") || perfil.equals("1") || perfil.equals("2")) {
                    String sql = "UPDATE archivo_impresion SET estado=0 WHERE id=?";
                    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                    pst.setString(1, id);

                    if (conexion.Update(pst) >= 0) {
                        conexion.Commit();
                        out.print("OK");

                    } else {
                        out.print("Error al eliminar el archivo");
                    }

                } else {
                    String sql = "SELECT * from archivo_impresion WHERE id=?";
                    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                    pst.setString(1, id);
                    java.sql.ResultSet rs = conexion.Query(pst);
                    if (rs.next()) {
                        String user_creador = (String) rs.getString("usuario");
                        if (user_creador.equals(usuario)) {
                            sql = "UPDATE archivo_impresion SET estado=0 WHERE id=?";
                            java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                            pst2.setString(1, id);

                            if (conexion.Update(pst2) >= 0) {
                                conexion.Commit();
                                out.print("OK");

                            } else {
                                out.print("Error al eliminar el archivo");
                            }

                        } else {
                            out.print("Usuario no autorizado para eliminar el archivo");
                        }

                    } else {
                        out.print("Id de archino no encontrado");
                    }

                }

            } catch (SQLException e) {
                out.print("Error. " + e.getMessage());
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
