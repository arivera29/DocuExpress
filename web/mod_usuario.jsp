<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%@ page import="java.util.*" %>
<%
	String userId = "";
	if (request.getParameter("usuario") == null) {
		response.sendRedirect("adduser.jsp");
		return;
	}
	String key = (String)request.getParameter("usuario");
	db conexion = new db();
	ControladorUsuarios manejador = new ControladorUsuarios(conexion);
	ControladorPerfil cp = new ControladorPerfil(conexion);
	ArrayList<Perfiles> p = cp.list();
	
	if (!manejador.exist(key) ) {
		response.sendRedirect("usuario.jsp");
		return;
	}
	Usuarios usuario = manejador.getUsuario();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modificar Usuario</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<script type="text/javascript">
function modificar(key) {
	var usuario = $("#usuario").val();
	var nombre = $("#nombre").val();
	var clave = $("#clave").val();
	var clave2 = $("#re_clave").val();
	var perfil = $("#perfil").val();
	var estado = $("#estado").val();
	
	if (usuario == "" || nombre == "" || perfil == "" ) {
		$("#info").html("<img src=\"warning.jpg\">Falta ingresar informacion");
		return;
	}
	
	if (clave != "") {
		if (clave != clave2) {
			alert("Las contraseñas no son iguales, verifique porfavor");
			return;
		}
	}
	
	
		var url = "SrvUsuarios";
		$.post(url,{
			operacion : "update",
			usuario : usuario,
			nombre: nombre,
			perfil: perfil,
			clave: clave,
			estado: estado,
			key:key
		},
		procesar);
	
	
}
function procesar(resultado) {
	if (resultado != 'OK') {
		$("#info").html("<img src=\"images/alerta.gif\"> " + resultado);
	}else {
		$("#info").html("<img src=\"images/ok.gif\"> Usuario modificado");
	}
}
	
 	function cancelar() {
 		document.location.href="usuario.jsp";
	}


</script>
</head>
<body>
<%@ include file="header.jsp"%>
<div class="container">
<h3>Modificar Usuario</h3>
<p>
	Esta vista permite modificar el registro de Usuario.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form name="form1" action="">
	<div class="form-group">
		<div class="row">
			<div class="col-xs-2">
				<label for="usuario">Id Usuario</label>
				<input type="text" class="form-control input-sm" id="usuario" name="usuario" value="<%= usuario.getUsuario() %>"  readonly>
			</div>
			<div class="col-xs-4">
				<label for="nombre">Nombre</label>
				<input type="text"  class="form-control input-sm" id="nombre" name="nombre" value="<%= usuario.getNombre() %>">
			</div>
			<div class="col-xs-2">
				<label for="perfil">Perfil</label>
				<select name="perfil" id="perfil" class="form-control input-sm">
				<option value="-1">Seleccionar</option>
				<% for (Perfiles a : p) { %>
					<option value="<%= a.getId() %>" <%= a.getId().equals(usuario.getPerfil())?"selected":"" %> ><%= a.getPerfil() %></option>
				<% } %>
				</select>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-2">
				<label for="clave">Contraseña</label>
				<input type="password" class="form-control input-sm" id="clave" name="clave">
			</div>
			<div class="col-xs-2">
				<label for="re_clave">Repetir contraseña</label>
				<input type="password" class="form-control input-sm" id="re_clave" name="re_clave">
			</div>
			<div class="col-xs-2">
				<label for="estado">Activo</label>
				<select id="estado" name="estado" class="form-control input-sm">
					<option value="1" <%= usuario.getEstado().equals("1")?"selected":"" %>>Si</option>
					<option value="0" <%= usuario.getEstado().equals("0")?"selected":"" %>>No</option>
				</select>
			</div>
		</div>
	</div>
	<div class="form-group">
		<button type="button" class="btn btn-primary" name="cmd_modificar" id="cmd_modificar" onclick="javascript:modificar('<%= (String)request.getParameter("usuario") %>');">Modificar</button> 
		<button type="button" class="btn btn-primary" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar();">Cancelar</button>
	</div>
</form>
</div>
<%@ include file="foot.jsp" %>
</body>
<script src="js/bootstrap.min.js"></script>
</html>

<%
	if (conexion != null) conexion.Close();
%>