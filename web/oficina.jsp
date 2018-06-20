<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%
    db conexion = new db();
    ControladorDelegacion c1 = new ControladorDelegacion(conexion);
    ControladorDepartamento c2 = new ControladorDepartamento(conexion);
    ArrayList<Delegacion> lista1 = c1.list();
    ArrayList<Departamento> lista2 = c2.list();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Oficina Comercial</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<link rel="stylesheet" href="js/bootstrap-table.css">
<script src="js/bootstrap-table.js"></script>
<script type="text/javascript">
	function agregar() {
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
			
			var cmd = document.getElementById("cmd_agregar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvOficina",
				{
					operacion: "add",
					codigo: codigo,
					nombre: nombre,
                                        direccion : direccion,
                                        delegacion : delegacion,
                                        departamento : departamento,
                                        estado : estado
				},
				procesar
			
			);
	
	}
	
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_agregar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"images/warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.gif\">Registro agregado correctamente");
			$("#codigo").val("");
			$("#nombre").val("");
                        $("#direcion").val("");
			list();
		}
		
	}

	function list() {
		$("#list").html("<img src=\"images/loading.gif\" > Consultando...");
		$("#list").load("SrvOficina?operacion=list",function() {
			$(".table").bootstrapTable({
	            pagination: true,
	            paginationLoop: false,
	            search :true
	        });
		});
	}
	
</script>
</head>
<body onload="list()">
<%@ include file="header.jsp" %>
<div class="container">
<h3>Oficina Comercial</h3>
<p>
	Esta vista permite agregar el registro de Oficina Comercial.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form action="" name="form1">
		<div class="form-group">
			<div class="row">
				<div class="col-xs-2">
					<label for="codigo">Codigo</label>
					<input type="text" name="codigo" id="codigo" class="form-control input-sm" placeholder="Ingrese codigo">
				</div>
				<div class="col-xs-4">
					<label for="nombre">Nombre</label>
					<input type="text" name="nombre" id="nombre" class="form-control input-sm" placeholder="Ingrese Nombre">
				</div>
                                <div class="col-xs-4">
					<label for="direccion">Direccion</label>
					<input type="text" name="direccion" id="direccion" class="form-control input-sm" placeholder="Ingrese direccion">
				</div>
			</div>
                        <div class="row">
				<div class="col-xs-2">
					<label for="delegacion">Delegacion</label>
                                        <select name="delegacion" id="delegacion" class="form-control input-sm">
                                            <option value="">Seleccionar</option>
                                            <% for (Delegacion delegacion : lista1) { %>
                                            <option value="<%= delegacion.getCodigo()  %>"><%= delegacion.getNombre()  %></option>
                                            <% } %>
                                        </select>
				</div>
				<div class="col-xs-4">
					<label for="departamento">Departamento</label>
					<select name="departamento" id="departamento" class="form-control input-sm">
                                            <option value="">Seleccionar</option>
                                            <% for (Departamento departamento : lista2) { %>
                                            <option value="<%= departamento.getCodigo()  %>"><%= departamento.getNombre()  %></option>
                                            <% } %>
                                        </select>
				</div>
                                <div class="col-xs-3">
					<label for="estado">Activo</label>
					<select name="estado" id="estado" class="form-control input-sm">
					<option value="1">Si</option>
					<option value="0">No</option>
                                        </select>
                                </div>
                                
			</div>
		</div>
		
		<div class="form-group">
			<button type="button" class="btn btn-primary" name="cmd_agregar" id="cmd_agregar" onclick="javascript:agregar()" >Agregar</button>
		</div>
	</form>
	
	<div id="list"></div>
	</div>
	<%@ include file="foot.jsp" %>
</body>
<script src="js/bootstrap.min.js"></script>
</html>
<%
    conexion.Close();
%>