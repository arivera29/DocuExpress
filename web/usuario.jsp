<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%@ page import="java.util.*" %>

<%
	db conexion = new db();
	ControladorPerfil manejador = new ControladorPerfil(conexion);
	ArrayList<Perfiles> p = manejador.list();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Usuarios</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<link rel="stylesheet" href="js/bootstrap-table.css">
<script src="js/bootstrap-table.js"></script>
<script type="text/javascript">
function agregar() {
	var usuario = $("#usuario").val();
	var nombre = $("#nombre").val();
	var clave = $("#clave").val();
	var clave2 = $("#re_clave").val();
	var perfil = $("#perfil").val();
	var estado = $("#estado").val();
	
	if (usuario == "" || nombre == "" || perfil == "-1" || clave == "" || clave2 == "") {
		$("#info").html("<img src=\"images/alerta.gif\">Falta ingresar informacion");
		return;
	}
	
	if (clave2 != clave) {
		$("#info").html("<img src=\"images/alerta.gif\">Las contraseñas deben ser iguales");
		return;
	}
		var url = "SrvUsuarios";
		$.post(url,{
			operacion : "add",
			usuario : usuario,
			nombre: nombre,
			perfil: perfil,
			clave: clave,
			estado: estado			
		},
		procesar);
	
	
}
function procesar(resultado) {
	if (resultado != 'OK') {
		$("#info").html(resultado);
	}else {
		$("#info").html("<img src=\"images/ok.gif\">Usuario agregado");
		$("#usuario").val("");
		$("#nombre").val("");
		$("#clave").val("");
		$("#re_clave").val("");
		lista();
	}
}
	
 	
 	
 	function buscar() {
 		var criterio = $("#criterio").val();
 		if (criterio == "") {
 			alert("Debe ingresar el criterio de busqueda");
 			return;
 		}
 		$("#list").load("SrvUsuarios?operacion=list&criterio=" + criterio, function() {
 			$(".table").bootstrapTable({
	            pagination: true,
	            paginationLoop: false,
	            search :true
	        });
 		});
 	}
 	
 	function lista() {
 		$("#list").html("<img src=\"images/loading.gif\" > Consultando...");
 		$("#list").load("SrvUsuarios", {
 			operacion : "list"
 		}, function (data) {
 			$(".table").bootstrapTable({
	            pagination: true,
	            paginationLoop: false,
	            search :true
	        });
 		});
 	}


</script>
</head>
<body onload="javascript:lista();">
<%@ include file="header.jsp"%>
<div class="container">
<h3>Usuarios</h3>
<p>
	Esta vista permite agregar el registro de Usuario.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form action="" id="form1" name="form1">
	<div class="form-group">
		<div class="row">
			<div class="col-xs-2">
				<label for="usuario">Id Usuario</label>
				<input type="text" class="form-control input-sm" placeholder="Ingrese Id usuario" id="usuario" name="usuario">
			</div>
			<div class="col-xs-4">
				<label for="nombre">Nombre</label>
				<input type="text" class="form-control input-sm" placeholder="Ingrese Nombre" id="nombre" name="nombre">
			</div>
			<div class="col-xs-2">
				<label for="perfil">Perfil</label>
				<select name="perfil" id="perfil" class="form-control input-sm">
					<option value="-1">Seleccionar</option>
					<% for (Perfiles a : p) { %>
					<option value="<%= a.getId() %>"><%= a.getPerfil() %></option>
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
				<select id="estado" name="estado"  class="form-control input-sm">
					<option value="1">Si</option>
					<option value="0">No</option>
				</select>
			</div>
		</div>
	</div>
	<div class="form-group">
		<button type="button" name="cmd_agregar" id="cmd_agregar" class="btn btn-primary" onclick="javascript:agregar();">Agregar</button>
	</div> 
</form>
<h4>Buscar Usuario</h4>
<form name="form2" action=""  class="form-inline">
<div class="form-group">
	<label for="criterio">Criterio</label>
	<input type="text" class="form-control input-sm" placeholder="Ingrese criterio" name="criterio" id="criterio" value="">
	<button type="button" class="btn btn-primary  btn-sm"  onclick="javascript:buscar();">Buscar</button>
</div>

</form>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
<script src="js/bootstrap.min.js"></script>
</html>

<%
	if (conexion != null) conexion.Close();
%>