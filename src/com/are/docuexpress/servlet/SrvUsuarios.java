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
import java.util.HashMap;

/**
 * Servlet implementation class SrvUsuarios
 */
@WebServlet("/SrvUsuarios")
public class SrvUsuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvUsuarios() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
                
                
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		try {

			if (operacion.equals("add")) { // agregar departamento
				String usuario = (String) request.getParameter("usuario");
				String nombre = (String) request.getParameter("nombre");
				String perfil = (String) request.getParameter("perfil");
				String clave = (String) request.getParameter("clave");
				String estado = (String) request.getParameter("estado");

				

				conexion = new db();
				Usuarios user = new Usuarios();
				user.setUsuario(usuario);
				user.setNombre(nombre);
				user.setPerfil(perfil);
				user.setClave(clave);
				user.setEstado(estado);


				ControladorUsuarios controlador = new ControladorUsuarios(conexion);
				
				if (!controlador.exist(usuario)) {
					if (controlador.add(user)) {
						out.print("OK");
					} else {
						out.print("Error al procesar la solicitud");
					}
				} else {
					out.print("Registro ya existe. ID=" + user.getNombre());
				}

			}

			if (operacion.equals("update")) { // Update departamento
				
				String usuario = (String) request.getParameter("usuario");
				String key = (String) request.getParameter("key");
				String nombre = (String) request.getParameter("nombre");
				String perfil = (String) request.getParameter("perfil");
				String clave = (String) request.getParameter("clave");
				String estado = (String) request.getParameter("estado");

				
				conexion = new db();
				
				Usuarios user = new Usuarios();
				user.setUsuario(usuario);
				user.setNombre(nombre);
				user.setPerfil(perfil);
				user.setClave(clave);
				user.setEstado(estado);

				
				
				ControladorUsuarios controlador = new ControladorUsuarios(conexion);

				if (controlador.exist(key)) {
					if (controlador.update(user,key)) {
						out.print("OK");
					} else {
						out.print("Error al procesar la solicitud");
					}
				} else {
					out.print("Registro no existe: ID=" + usuario);
				}

			}

			
			if (operacion.equals("list")) {

				conexion = new db();
				String criterio = "";
				String sql = "select usuario,nombre,estado,perfiles.perfil from usuarios,perfiles where usuarios.perfil = perfiles.id ";
                                
                                
                                
				
				if (request.getParameter("criterio") != null) {
					criterio = (String) request.getParameter("criterio");
                                        criterio = "%" + criterio + "%";
					sql += String.format(" and (usuario like '%s' OR nombre like '%s')", criterio,criterio);
				}
                                
                                sql += " order by usuario";
                                
                                java.sql.ResultSet rs = conexion.Query(sql);
                                ArrayList<HashMap> usuarios = conexion.resultSetToArrayList(rs);
				

				out.println("<table class=\"table table-striped\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Usuario</th>");
				out.println("<th>Nombre</th>");
				out.println("<th>Perfil</th>");
				out.println("<th>Estado</th>");
				out.println("<th></th>");
				out.println("</tr>");
				out.println("</thead>");
				int contador = 0;
				out.println("<tbody>");
				for (HashMap row : usuarios) {
						if (contador % 2 == 0) {
							out.println("<tr class='odd'>");
						} else {
							out.println("<tr>");
						}
						out.println("<td>" + row.get("usuario") + "</td>");
						out.println("<td>" + row.get("nombre") + "</td>");
						out.println("<td>" + row.get("perfil") + "</td>");
						out.println("<td>");
						if (row.get("estado").toString().equals("1")) {
							out.print("<img src=\"images/online.png\">");
						}else {
							out.print("<img src=\"images/offline.png\">");
						}
						out.print("</td>");
						out.println("<td>");
						out.print("<a class=\"btn btn-primary btn-sm\" href=\"mod_usuario.jsp?usuario="+ row.get("usuario") + "\">Editar</a>");
						out.println("</td>");
						out.println("</tr>");
						contador++;

					}
				out.println("</tbody>");
				out.println("</table>");
				

			}
			

			if (operacion.equals("changepassword")) {
				String user = (String) request.getParameter("usuario");
				String clave = (String) request.getParameter("clave");

				conexion = new db();
				ControladorUsuarios controlador = new ControladorUsuarios(conexion);
				if (controlador.changepassword(user, clave)) {
					out.print("OK");
				} 

			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: " + e.getMessage());
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
