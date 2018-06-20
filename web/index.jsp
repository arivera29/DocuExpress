<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="com.are.docuexpress.controlador.*"%>
<%@ page import="com.are.docuexpress.entidad.*"%>
<%@ page import="java.sql.*"%>
<%
	ControladorSuscripcion controlador = new ControladorSuscripcion();
	if (!controlador.verify()) {
		response.sendRedirect("suspend.jsp");
		return;
	}

	int flag = 0;
	if (request.getParameter("usuario") != null
			&& request.getParameter("clave") != null) {
		String usuario = (String) request.getParameter("usuario");
		String password = (String) request.getParameter("clave");
		db conexion = new db();
		ControladorUsuarios manager = new ControladorUsuarios(conexion);
		if (manager.login(usuario, password)) {
			conexion.Close();
			Usuarios user = manager.getUsuario();
			session.setAttribute("usuario", user.getUsuario());
			session.setAttribute("perfil", user.getPerfil());
			response.sendRedirect("main.jsp");
			return;
		} else {
			conexion.Close();
			response.sendRedirect("index.jsp?error=1");
			return;
		}
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HGC</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/imagen.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<script type="text/javascript">
	function validar() {
		var usuario = $("#usuario").val();
		var clave = $("#password").val();
		if (usuario == "" || clave == "") {
			$("#info").html("Faltan datos");
			return false;
		}

		return true;
	}

</script>
<style>
    img {
    margin: 25px;
    opacity: 0.8;
    border: 10px solid #eee;
    -webkit-transition: all 0.5s ease;
    -moz-transition: all 0.5s ease;
    -o-transition: all 0.5s ease;
    -webkit-box-reflect: below 0px -webkit-gradient(linear, left top, left bottom, from(transparent), color-stop(.7, transparent), to(rgba(0,0,0,0.1)));
    
}
 
img:hover {
    opacity: 1;
    -webkit-box-reflect: below 0px -webkit-gradient(linear, left top, left bottom, from(transparent), color-stop(.7, transparent), to(rgba(0,0,0,0.4)));
    -webkit-box-shadow: 0px 0px 20px rgba(255,255,255,0.8);
    -moz-box-shadow: 0px 0px 20px rgba(255,255,255,0.8);
    box-shadow: 0px 0px 20px rgba(255,255,255,0.8);
}
</style>
</head>
<body>
<div class="container">
            <!-- Static navbar -->
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="#">ELECTRICARIBE</a>
                    </div>
                    
                </div><!--/.container-fluid -->
            </nav> 
            
                <h1>Login</h1>
                <form name="form1" action="" method="post" onsubmit="javascript:return validar();">
                    <div class="row">
                    	<div class="col-xs-3"> 
		                    <div class="well">
		                    	<div class="form-group">
				                    <label for="usuario">Usuario</label><br> 
				                    <input type="text" class="form-control"  id="usuario" name="usuario" /><br>
				                    <label for="clave">Clave</label><br> 
				                    <input type="password" class="form-control" id="clave" name="clave" />
			                    </div>
			                    <div class="form-group">
	                    				<button class="btn btn-primary" role="button">Login</button>
	                    				<div id="info"></div>
                				</div>
		                    </div>
	                   </div>
	                   <div class="col-xs-3">
				<img src="images/electricaribe.jpg">
	                   </div>
                    </div>
                </form>
                
                
            </div>
</body>
</html>