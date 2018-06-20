<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("Empresa.jsp");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	ControladorEmpresa manejador = new ControladorEmpresa(conexion);
	if (!manejador.Find(codigo)) {
		response.sendRedirect("Empresa.jsp");
		return;
	}
	
	Empresa empresa = manejador.getEmpresa();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modificar Empresa</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<script type="text/javascript">
	function modificar(key) {
		var codigo = $("#codigo").val();
		var nombre = $("#nombre").val();
		var estado = $("#estado").val();
		
		if (codigo == "" || nombre == "") {
			$("#info").html("<img src=\"images/warning.jpg\">Faltan datos");
			return;
		}
		var cmd = document.getElementById("cmd_modificar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"SrvEmpresa",
			{
				operacion: "update",
				codigo: codigo,
				nombre: nombre,
				estado: estado,
				key: key
			},
			procesar
		
		);
		
	}
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_modificar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"images/warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.gif\">Empresa modificada correctamente");
		}
	}

	function eliminar(key) {
		if (confirm("Desea eliminar la Empresa " + key)) {
			var cmd = document.getElementById("cmd_eliminar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvEmpresa",
				{
					operacion: "remove",
					key: key
				},
				procesarEliminar
			
			);
	}
		
	}
	
	function procesarEliminar(resultado) {
		var cmd = document.getElementById("cmd_eliminar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"images/warning.jpg\">" + resultado);
		}else {
			alert("Empresa eliminada correctamente");
			window.location.href = "Empresa.jsp";
		}
		
	}
	
	function cancelar() {
		window.location.href="Empresa.jsp";
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
<h3>Modificar Empresa</h3>
<p>
	Esta vista permite modificar el registro de Empresa.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form action="" name="form1">
		<div class="form-group">
			<div class="row">
				<div class="col-xs-2">
					<label for="codigo">Codigo</label>
					<input type="text" name="codigo" id="codigo" class="form-control input-sm" placeholder="Ingrese codigo" value="<%= empresa.getCodigo()  %>">
				</div>
				<div class="col-xs-4">
					<label for="nombre">Nombre</label>
					<input type="text" name="nombre" id="nombre" class="form-control input-sm" placeholder="Ingrese descripcion" value="<%= empresa.getNombre()  %>">
				</div>
				<div class="col-xs-3">
					<label for="estado">Activo</label>
					<select name="estado" id="estado" class="form-control input-sm">
					<option value="1" <%= empresa.getEstado()==1?"selected":"" %>  >Si</option>
					<option value="0" <%= empresa.getEstado()==0?"selected":"" %>>No</option>
				</select>
				</div>
			
			</div>
		
		</div>
		<div class="form-group">
			<button type="button" class="btn btn-primary" onclick="javascript:modificar('<%= (String)request.getParameter("codigo") %>');" id="cmd_modificar" name="cmd_modificar" >Actualizar</button> 
			<button type="button" class="btn btn-primary" onclick="javascript:eliminar('<%= (String)request.getParameter("codigo") %>');" id="cmd_eliminar" name="cmd_eliminar" >Eliminar</button>  
			<button type="button" class="btn btn-primary" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar()" >Cancelar</button>
		</div> 
	</form>
	</div>
	<%@ include file="foot.jsp" %>
</body>
<script src="js/bootstrap.min.js"></script>
</html>

<%
	conexion.Close();
%>