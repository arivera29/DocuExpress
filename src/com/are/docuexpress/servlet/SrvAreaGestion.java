/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.ControladorAreaGestion;
import com.are.docuexpress.controlador.db;
import com.are.docuexpress.entidad.AreaGestion;
import com.are.docuexpress.entidad.Delegacion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
@WebServlet("/SrvAreaGestion")
public class SrvAreaGestion extends HttpServlet {

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
            db conexion = null;
            try {
                /*
                 Operacion add: Agregar registro
                 */
                if (operacion.equals("add")) {
                    conexion = new db();
                    String nombre = (String) request.getParameter("nombre");
                    String estado = (String) request.getParameter("estado");
                    ControladorAreaGestion controlador = new ControladorAreaGestion(conexion);

                    AreaGestion area = new AreaGestion();

                    area.setNombre(nombre);
                    area.setEstado(Integer.parseInt(estado));
                    if (controlador.add(area)) {
                        out.print("OK");
                    } else {
                        out.print("Error al agregar el registro");
                    }
                }
                /*
                 Operacion update: Actualizar registro
                 */
                if (operacion.equals("update")) {
                    conexion = new db();
                    String key = (String) request.getParameter("key");
                    String nombre = (String) request.getParameter("nombre");
                    String estado = (String) request.getParameter("estado");

                    ControladorAreaGestion controlador = new ControladorAreaGestion(conexion);

                    if (controlador.find(Integer.parseInt(key))) {
                        AreaGestion area = new AreaGestion();
                        area.setNombre(nombre);
                        area.setEstado(Integer.parseInt(estado));
                        if (controlador.update(area, Integer.parseInt(key))) {
                            out.print("OK");
                        } else {
                            out.print("Error al actualizar el registro");
                        }
                    } else {
                        out.print("ID no se encuentra registrado");
                    }
                }

                /*
                 Operacion remove: Eliminar registro
                 */
                if (operacion.equals("remove")) {
                    conexion = new db();
                    String key = (String) request.getParameter("key");
                    ControladorAreaGestion controlador = new ControladorAreaGestion(conexion);

                    if (controlador.find(Integer.parseInt(key))) {
                        Delegacion delegacion = new Delegacion();
                        if (controlador.remove(Integer.parseInt(key))) {
                            out.print("OK");
                        } else {
                            out.print("Error al eliminar el registro");
                        }
                    } else {
                        out.print("ID no se encuentra registrado");
                    }
                }

                /*
                 Operacion list: listado de registros
                 */
                if (operacion.equals("list")) {
                    conexion = new db();
                    ControladorAreaGestion controlador = new ControladorAreaGestion(conexion);
                    ArrayList<AreaGestion> lista = controlador.list();
                    out.println("<table class='table' id='tabla'>");

                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th>ACCION</th>");
                    out.println("</tr>");
                    out.println("</thead>");

                    out.println("<tbody>");
                    for (AreaGestion area : lista) {
                        out.println("<tr>");
                        out.println("<td>" + area.getId() + "</td>");
                        out.println("<td>" + area.getNombre() + "</td>");
                        out.println("<td>" + area.getEstado() + "</td>");
                        out.println("<td><a class='btn btn-primary bt-sm' href='mod_areagestion.jsp?codigo=" + area.getId() + "'>Editar</a></td>");
                        out.println("<td><a class='btn btn-primary bt-sm' href='correo_areagestion.jsp?codigo=" + area.getId() + "'>Correos</a></td>");
                        out.println("</tr>");
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                }
                
                if (operacion.equals("addCorreo")) {
                    conexion = new db();
                    String key = (String) request.getParameter("key");
                    String correo = (String) request.getParameter("correo");
                    ControladorAreaGestion controlador = new ControladorAreaGestion(conexion);
                    if (controlador.addCorreo(Integer.parseInt(key),correo)) {
                        out.print("OK");
                    } else {
                        out.print("Error al agregar el registro");
                    }
                }
                
                if (operacion.equals("removeCorreo")) {
                    conexion = new db();
                    String key = (String) request.getParameter("key");
                    ControladorAreaGestion controlador = new ControladorAreaGestion(conexion);
                    if (controlador.removeCorreo(Integer.parseInt(key))) {
                        out.print("OK");
                    } else {
                        out.print("Error al eliminar el registro");
                    }
                }
                
                /*
                 Operacion list: listado de registros
                 */
                if (operacion.equals("listCorreo")) {
                    String key = (String) request.getParameter("key");
                    conexion = new db();
                    String sql = "SELECT idCorreo,correo FROM correoAreaGestion WHERE codArea=?";
                    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(key));
                    java.sql.ResultSet rs = conexion.Query(pst);
                    out.println("<table class='table' id='tabla'>");

                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th>ACCION</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    while(rs.next()) {
                        out.println("<tr>");
                        out.println("<td>" + rs.getString("idCorreo") + "</td>");
                        out.println("<td>" + rs.getString("correo") + "</td>");
                        out.println("<td><a class='btn btn-primary bt-sm' href='javascript:eliminar(" + key + "," + rs.getInt("idCorreo") + ");'>Eliminar</a></td>");
                        out.println("</tr>");
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                }

            } catch (SQLException e) {
                out.print("Error: " + e.getMessage());
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();

                    } catch (SQLException ex) {
                        Logger.getLogger(SrvAreaGestion.class.getName()).log(Level.SEVERE, null, ex);
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
