/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.ControladorNoConformidad;
import com.are.docuexpress.controlador.db;
import com.are.docuexpress.entidad.NoConformidad;
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
@WebServlet("/SrvNoConformidad")
public class SrvNoConformidad extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            String operacion = (String) request.getParameter("operacion");

            HttpSession session = request.getSession(true);
            String usuario = (String) session.getAttribute("usuario");

            db conexion = null;
            try {
                /*
                 Operacion add: Agregar registro
                 */
                if (operacion.equals("add")) {
                    conexion = new db();
                    String id = (String) request.getParameter("id");
                    String observacion = (String) request.getParameter("observacion");
                    String tipo = (String) request.getParameter("tipo");
                    String _tipo = (String) request.getParameter("_tipo");

                    ControladorNoConformidad controlador = new ControladorNoConformidad(conexion);

                    NoConformidad nc = new NoConformidad();
                    nc.setTipo(Integer.parseInt(_tipo));
                    nc.setTipoNoConformidad(tipo);
                    if (nc.getTipo() == 1) {
                        nc.setIdArchivo(Integer.parseInt(id));
                    } else {
                        nc.setIdDocumento(Integer.parseInt(id));
                    }
                    nc.setObservacion(observacion);
                    nc.setUsuario(usuario);

                    nc.setEstado(1);

                    if (controlador.add(nc)) {
                        out.print("OK");
                    } else {
                        out.print("Error al agregar el registro");
                    }

                }

            } catch (SQLException e) {
                out.print("Error: " + e.getMessage());
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();

                    } catch (SQLException ex) {
                        Logger.getLogger(SrvNoConformidad.class.getName()).log(Level.SEVERE, null, ex);
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
