package com.are.docuexpress.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.are.docuexpress.controlador.*;
import com.are.docuexpress.entidad.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet implementation class srvCargos
 */
@WebServlet("/SrvSmartphone")
public class SrvSmartphone extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvSmartphone() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
                    String imei = (String)request.getParameter("imei");
                    String estado = (String)request.getParameter("estado");
                    String empresa = (String)request.getParameter("empresa");
                    
                    ControladorSmartphone controlador = new ControladorSmartphone(conexion);
                    
                    if (!controlador.find(imei)) {
                        Smartphone smartphone = new Smartphone();
                        smartphone.setIMEI(imei);
                        smartphone.setEstado(Integer.parseInt(estado));
                        smartphone.setEmpresa(empresa);
                        
                        if (controlador.add(smartphone)) {
                            out.print("OK");
                        }else {
                            out.print("Error al agregar el registro");
                        }
                    }else {
                        out.print("IMEI ya se encuentra registrado");
                    }
                }
                /*
                    Operacion update: Actualizar registro
                */
                if (operacion.equals("update")) { 
                    conexion = new db();
                    String key = (String)request.getParameter("key");
                    String imei = (String)request.getParameter("imei");
                    String estado = (String)request.getParameter("estado");
                    String empresa = (String)request.getParameter("empresa");
                    
                    ControladorSmartphone controlador = new ControladorSmartphone(conexion);
                    
                    if (controlador.find(key)) {
                        Smartphone smartphone = new Smartphone();
                        smartphone.setIMEI(imei);
                        smartphone.setEstado(Integer.parseInt(estado));
                        smartphone.setEmpresa(empresa);
                        if (controlador.update(smartphone,key)) {
                            out.print("OK");
                        }else {
                            out.print("Error al actualizar el registro");
                        }
                    }else {
                        out.print("IMEI no se encuentra registrado");
                    }
                }
                
                /*
                    Operacion remove: Eliminar registro
                */
                if (operacion.equals("remove")) { 
                    conexion = new db();
                    String key = (String)request.getParameter("key");
                    ControladorSmartphone controlador = new ControladorSmartphone(conexion);
                    
                    if (controlador.find(key)) {
                        if (controlador.remove(key)) {
                            out.print("OK");
                        }else {
                            out.print("Error al eliminar el registro");
                        }
                    }else {
                        out.print("IMEI no se encuentra registrado");
                    }
                }
                
                /*
                    Operacion list: listado de registros
                */
                if (operacion.equals("list")) { 
                    conexion = new db();
                    ControladorSmartphone controlador = new ControladorSmartphone(conexion);
                    ArrayList<Smartphone> lista = controlador.list();
                    out.println("<table class='table' id='tabla'>");
                    
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>IMEI</th>");
                    out.println("<th>EMPRESA</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th>ACCION</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    
                    out.println("<tbody>");
                    for (Smartphone smartphone : lista) {
                        out.println("<tr>");
                        out.println("<td>" + smartphone.getIMEI() + "</td>");
                        out.println("<td>" + smartphone.getEmpresa() + "</td>");
                        out.println("<td>" + smartphone.getEstado() + "</td>");
                        out.println("<td><a class='btn btn-primary bt-sm' href='mod_smartphone.jsp?codigo=" + smartphone.getIMEI() + "'>Editar</a></td>");
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
        ProcesarPeticion(request, response);
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
        ProcesarPeticion(request, response);
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
