/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.ControladorDelegacion;
import com.are.docuexpress.controlador.db;
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
@WebServlet("/SrvDelegacion")
public class SrvDelegacion extends HttpServlet {

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
            String operacion = (String)request.getParameter("operacion");
            db conexion = null;
            try {
                /*
                    Operacion add: Agregar registro
                */
                if (operacion.equals("add")) { 
                    conexion = new db();
                    String codigo = (String)request.getParameter("codigo");
                    String nombre = (String)request.getParameter("nombre");
                    ControladorDelegacion controlador = new ControladorDelegacion(conexion);
                    
                    if (!controlador.find(codigo)) {
                        Delegacion delegacion = new Delegacion();
                        delegacion.setCodigo(codigo);
                        delegacion.setNombre(nombre);
                        if (controlador.add(delegacion)) {
                            out.print("OK");
                        }else {
                            out.print("Error al agregar el registro");
                        }
                    }else {
                        out.print("Codigo de delegacion ya se encuentra registrado");
                    }
                }
                /*
                    Operacion update: Actualizar registro
                */
                if (operacion.equals("update")) { 
                    conexion = new db();
                    String key = (String)request.getParameter("key");
                    String codigo = (String)request.getParameter("codigo");
                    String nombre = (String)request.getParameter("nombre");
                    ControladorDelegacion controlador = new ControladorDelegacion(conexion);
                    
                    if (controlador.find(key)) {
                        Delegacion delegacion = new Delegacion();
                        delegacion.setCodigo(codigo);
                        delegacion.setNombre(nombre);
                        if (controlador.update(delegacion,key)) {
                            out.print("OK");
                        }else {
                            out.print("Error al actualizar el registro");
                        }
                    }else {
                        out.print("Codigo de delegacion no se encuentra registrado");
                    }
                }
                
                /*
                    Operacion remove: Eliminar registro
                */
                if (operacion.equals("remove")) { 
                    conexion = new db();
                    String key = (String)request.getParameter("key");
                    ControladorDelegacion controlador = new ControladorDelegacion(conexion);
                    
                    if (controlador.find(key)) {
                        Delegacion delegacion = new Delegacion();
                        if (controlador.remove(key)) {
                            out.print("OK");
                        }else {
                            out.print("Error al eliminar el registro");
                        }
                    }else {
                        out.print("Codigo de delegacion no se encuentra registrado");
                    }
                }
                
                /*
                    Operacion list: listado de registros
                */
                if (operacion.equals("list")) { 
                    conexion = new db();
                    ControladorDelegacion controlador = new ControladorDelegacion(conexion);
                    ArrayList<Delegacion> lista = controlador.list();
                    out.println("<table class='table' id='tabla'>");
                    
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ACCION</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    
                    out.println("<tbody>");
                    for (Delegacion delegacion : lista) {
                        out.println("<tr>");
                        out.println("<td>" + delegacion.getCodigo() + "</td>");
                        out.println("<td>" + delegacion.getNombre() + "</td>");
                        out.println("<td><a class='btn btn-primary bt-sm' href='mod_delegacion.jsp?codigo=" + delegacion.getCodigo() + "'>Editar</a></td>");
                        out.println("</tr>");
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                }
                
            }catch (SQLException e)  {
                    out.print("Error: " + e.getMessage());
            }finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(SrvDelegacion.class.getName()).log(Level.SEVERE, null, ex);
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
