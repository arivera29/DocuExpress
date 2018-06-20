<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("smartphone.jsp");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	ControladorSmartphone manejador = new ControladorSmartphone(conexion);
	if (!manejador.find(codigo)) {
		response.sendRedirect("smartphone.jsp");
		return;
	}
	
	Smartphone smartphone = manejador.getSmartphone();
        ControladorEmpresa controlador = new ControladorEmpresa(conexion);
        ArrayList<Empresa> lista = controlador.List(1);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modificar Smartphone</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<script type="text/javascript">
	function modificar(key) {
		var imei = $("#imei").val();
		var empresa =  $("#empresa").val();
                var estado =  $("#estado").val();

		if (imei == "" || empresa == "") {
			$("#info").html("<img src=\"warning.jpg\">Falta informacion");
			return;
		}
                
		var cmd = document.getElementById("cmd_modificar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"SrvSmartphone",
			{
				operacion: "update",
				imei : imei,
				empresa : empresa,
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
		if (confirm("Desea eliminar el registro " + key)) {
			var cmd = document.getElementById("cmd_eliminar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvSmartphone",
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
			window.location.href = "smartphone.jsp";
		}
		
	}
	
	function cancelar() {
		window.location.href="smartphone.jsp";
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
<h3>Modificar Smartphone</h3>
<p>
	Esta vista permite modificar el registro de Smartphone.  Debe diligenciar la informacion y dar clic en el boton Agregar.
</p>
<div id="info"></div>
<form action="" name="form1">
		<div class="form-group">
			<div class="row">
				<div class="col-xs-2">
					<label for="imei">IMEI</label>
					<input type="text" name="imei" id="imei" class="form-control input-sm" placeholder="Ingrese codigo" value="<%= smartphone.getIMEI()  %>">
				</div>
				<div class="col-xs-4">
                                    <label for="empresa">Empresa</label>
                                    <select name="empresa" id="empresa" class="form-control input-sm">
					<option value="">Seleccionar</option>
                                        <% for (Empresa empresa : lista) { %>
                                        <option value="<%=  empresa.getCodigo() %>" <%= (empresa.getCodigo().equals(smartphone.getEmpresa()))?"selected":"" %>><%= empresa.getNombre() %></option>
                                        <% } %>
                                    </select>
				</div>
				<div class="col-xs-3">
					<label for="estado">Activo</label>
					<select name="estado" id="estado" class="form-control input-sm">
					<option value="1" <%= smartphone.getEstado()==1?"selected":"" %>  >Si</option>
					<option value="0" <%= smartphone.getEstado()==0?"selected":"" %>>No</option>
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