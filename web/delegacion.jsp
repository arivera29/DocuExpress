<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delegacion</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<link rel="stylesheet" href="js/bootstrap-table.css">
<script src="js/bootstrap-table.js"></script>
<script type="text/javascript">
	function agregar() {
		var codigo = $("#codigo").val();
		var nombre =  $("#nombre").val();

		if (codigo == "" || nombre == "") {
			$("#info").html("<img src=\"warning.jpg\">Falta informacion");
			return;
		}
			
			var cmd = document.getElementById("cmd_agregar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvDelegacion",
				{
					operacion: "add",
					codigo: codigo,
					nombre: nombre
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
			list();
		}
		
	}

	function list() {
		$("#list").html("<img src=\"images/loading.gif\" > Consultando...");
		$("#list").load("SrvDelegacion?operacion=list",function() {
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
<h3>Delegacion</h3>
<p>
	Esta vista permite agregar el registro de Delegacion.  Debe diligenciar la informacion y dar clic en el boton Agregar.
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
					<input type="text" name="nombre" id="nombre" class="form-control input-sm" placeholder="Ingrese descripcion">
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