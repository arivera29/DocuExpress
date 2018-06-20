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

/**
 * Servlet implementation class srvCargos
 */
@WebServlet("/SrvEmpresa")
public class SrvEmpresa extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvEmpresa() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		try {

		if (operacion.equals("add")) { // Agregar Empresa
			String codigo = (String) request.getParameter("codigo");
			String nombre = (String) request.getParameter("nombre");
			String estado = (String) request.getParameter("estado");
			
				conexion = new db();
				Empresa empresa = new Empresa();
				empresa.setCodigo(codigo);
				empresa.setNombre(nombre);
				empresa.setEstado(Integer.parseInt(estado));
				ControladorEmpresa controlador = new ControladorEmpresa(conexion);
				
				if (!controlador.Find(codigo)) {
					if (controlador.add(empresa)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Ya existe el registro. Id=" + codigo);
				}

			

		}



		if (operacion.equals("update")) { // Modificar Tipo de Orden
			String codigo = (String) request.getParameter("codigo");
			String nombre = (String) request.getParameter("nombre");
			String estado = (String) request.getParameter("estado");
			String key = (String) request.getParameter("key");
			
				conexion = new db();
				Empresa empresa = new Empresa();
				empresa.setCodigo(codigo);
				empresa.setNombre(nombre);
				empresa.setEstado(Integer.parseInt(estado));
				
				ControladorEmpresa controlador = new ControladorEmpresa(conexion);
				if (controlador.Find(key)) {
					if (controlador.update(empresa,key)) {
						out.print("OK");
					} else {
						out.print("Error al procesar la solicitud");
					}
				} else {
					out.print("Registro no existe. Id=" + key);
				}

		}
		
		
		if (operacion.equals("remove")) { // Eliminar Tipo de Orden

			String key = (String) request.getParameter("key");
			
				conexion = new db();
				ControladorEmpresa controlador = new ControladorEmpresa(conexion);
				if (controlador.Find(key)) {
					if (controlador.remove(key)) {
						out.print("OK");
					} else {
						out.print("Error al procesar la solicitud");
					}
				} else {
					out.print("Registro no encontrado. Id=" + key);
				}
		}
		
		

		if (operacion.equals("list")) {  // Listado de Tipo de ordenes
				conexion = new db();
				ControladorEmpresa controlador = new ControladorEmpresa(conexion);
				ArrayList<Empresa> lista = controlador.List();
				 
					out.println("<table class=\"table table-striped\">");
					out.println("<thead>");
					out.println("<tr>");
					out.println("<th>CODIGO</th>");
					out.println("<th>NOMBRE</th>");
					out.println("<th>ESTADO</th>");
					out.println("<th>ACCION</th>");
					out.println("</tr>");
					out.println("</thead>");
					int fila = 1;
					out.println("<tbody>");
					for (Empresa empresa : lista) {
						out.println("<tr "  +(fila%2==0?"class='odd'":"") + ">");
						out.println("<td>" + empresa.getCodigo() + "</td>");
						out.println("<td>" + empresa.getNombre() + "</td>");
						out.println("<td>");
						if (empresa.getEstado()==1) {
							out.print("<img src=\"images/online.png\">");
						}else {
							out.print("<img src=\"images/offline.png\">");
						}
						out.print("</td>");
						out.println("<td>");
						out.println("<a class=\"btn btn-primary btn-sm\" href=\"mod_empresa.jsp?codigo="+ empresa.getCodigo()+ "\">Editar</a>");
						out.print("</td>");
						out.println("</tr>");
						fila++;
					}
					out.println("</tbody>");
					out.println("</table>");
		}
		

		
		if (operacion.equals("combo")) { // combo de tipos de orden
				conexion = new db();
				ControladorEmpresa controlador = new ControladorEmpresa(conexion);
				ArrayList<Empresa> lista = controlador.List();

				out.println("<select id='empresa' name='empresa'>");
				out.println("<option value=''>Seleccionar</option>");
				
				if (lista.size() > 0) {
					for (Empresa empresa : lista) {
						out.println("<option value='" + empresa.getCodigo() + "'>" + empresa.getNombre() + "</option>");
					}
					
				}
				out.println("</select>");

		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: "	+ e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("Error. " + e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					out.print(e.getMessage());
				}
			}
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
