<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%
    db conexion = new db();
    ControladorEmpresa controlador = new ControladorEmpresa(conexion);
    ArrayList<Empresa> lista = controlador.List(1);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Smartphone</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<link rel="stylesheet" href="js/bootstrap-table.css">
<script src="js/bootstrap-table.js"></script>
<script type="text/javascript">
	function agregar() {
		var imei = $("#imei").val();
		var empresa =  $("#empresa").val();
                var estado =  $("#estado").val();

		if (imei == "" || empresa == "") {
			$("#info").html("<img src=\"warning.jpg\">Falta informacion");
			return;
		}
			
			var cmd = document.getElementById("cmd_agregar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvSmartphone",
				{
					operacion: "add",
					imei : imei,
					empresa : empresa,
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
			$("#imei").val("");
			list();
		}
		
	}

	function list() {
		$("#list").html("<img src=\"images/loading.gif\" > Consultando...");
		$("#list").load("SrvSmartphone?operacion=list",function() {
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
<h3>Smartphone</h3>
<p>
	Esta vista permite agregar el registro de Smartphone.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form action="" name="form1">
		<div class="form-group">
			<div class="row">
				<div class="col-xs-4">
					<label for="codigo">IMEI</label>
					<input type="text" name="imei" id="imei" class="form-control input-sm" placeholder="Ingrese codigo">
				</div>
				<div class="col-xs-4">
                                    <label for="empresa">Empresa</label>
                                    <select name="empresa" id="empresa" class="form-control input-sm">
					<option value="">Seleccionar</option>
                                        <% for (Empresa empresa : lista) { %>
					<option value="<%=  empresa.getCodigo() %>"><%= empresa.getNombre() %></option>
                                        <% } %>
                                    </select>
				</div>
                                <div class="col-xs-2">
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