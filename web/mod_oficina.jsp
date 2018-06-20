<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("oficina.jsp");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	ControladorOficina manejador = new ControladorOficina(conexion);
	if (!manejador.find(codigo)) {
		response.sendRedirect("oficina.jsp");
		return;
	}
	
	Oficina oficina = manejador.getOficina();
        
        ControladorDelegacion c1 = new ControladorDelegacion(conexion);
        ControladorDepartamento c2 = new ControladorDepartamento(conexion);
        ArrayList<Delegacion> lista1 = c1.list();
        ArrayList<Departamento> lista2 = c2.list();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modificar Oficina</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<script type="text/javascript">
	function modificar(key) {
		var codigo = $("#codigo").val();
		var nombre =  $("#nombre").val();
                var direccion =  $("#direccion").val();
                var delegacion =  $("#delegacion").val();
                var departamento =  $("#departamento").val();
                var estado = $("#estado").val();

		if (codigo == "" || nombre == "" || delegacion == "" || departamento == "") {
			$("#info").html("<img src=\"warning.jpg\">Falta informacion");
			return;
		}
		var cmd = document.getElementById("cmd_modificar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"SrvOficina",
			{
				operacion: "update",
				codigo: codigo,
				nombre: nombre,
                                direccion : direccion,
                                delegacion : delegacion,
                                departamento : departamento,
                                estado : estado,
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
			$("#info").html("<img src=\"images/ok.gif\">Registro modificado correctamente");
		}
	}

	function eliminar(key) {
		if (confirm("Desea eliminar la Empresa " + key)) {
			var cmd = document.getElementById("cmd_eliminar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvOficina",
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
			alert("Registro eliminado correctamente");
			window.location.href = "oficina.jsp";
		}
		
	}
	
	function cancelar() {
		window.location.href="oficina.jsp";
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
<h3>Modificar Oficina</h3>
<p>
	Esta vista permite modificar el registro de Oficina comercial.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form action="" name="form1">
		<div class="form-group">
			<div class="row">
				<div class="col-xs-2">
					<label for="codigo">Codigo</label>
					<input type="text" name="codigo" id="codigo" class="form-control input-sm" placeholder="Ingrese codigo" value="<%= oficina.getCodigo()  %>">
				</div>
				<div class="col-xs-4">
					<label for="nombre">Nombre</label>
					<input type="text" name="nombre" id="nombre" class="form-control input-sm" placeholder="Ingrese descripcion" value="<%= oficina.getNombre()  %>">
				</div>
				<div class="col-xs-4">
					<label for="direccion">Direccion</label>
					<input type="text" name="direccion" id="direccion" class="form-control input-sm" value="<%= oficina.getDireccion()  %>" placeholder="Ingrese direccion">
				</div>
			</div>
                                
                        <div class="row">
				<div class="col-xs-2">
					<label for="delegacion">Delegacion</label>
                                        <select name="delegacion" id="delegacion" class="form-control input-sm">
                                            <option value="">Seleccionar</option>
                                            <% for (Delegacion delegacion : lista1) { %>
                                            <option value="<%= delegacion.getCodigo()  %>" <%= (delegacion.getCodigo().equals(oficina.getDelegacion()))?"selected":"" %>><%= delegacion.getNombre()  %></option>
                                            <% } %>
                                        </select>
				</div>
				<div class="col-xs-4">
					<label for="departamento">Departamento</label>
					<select name="departamento" id="departamento" class="form-control input-sm">
                                            <option value="">Seleccionar</option>
                                            <% for (Departamento departamento : lista2) { %>
                                            <option value="<%= departamento.getCodigo()  %>" <%= (departamento.getCodigo().equals(oficina.getDepartamento()))?"selected":"" %>><%= departamento.getNombre()  %></option>
                                            <% } %>
                                        </select>
				</div>
                                <div class="col-xs-3">
                                    <label for="estado">Activo</label>
                                    <select name="estado" id="estado" class="form-control input-sm">
                                        <option value="1" <%= oficina.getEstado()==1?"selected":"" %>  >Si</option>
					<option value="0" <%= oficina.getEstado()==0?"selected":"" %>>No</option>
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