<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="com.are.docuexpress.controlador.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Conexion Database</title>
</head>
<body>
<h2>Test Connection Database SQL Server</h2>

<%
	db conexion = new db();
	if (conexion.getConnection() != null) {
		
		out.println("Conectado");
		conexion.Close();
	}else {
		out.println("Error al conectarse");
	}

%>

</body>
</html>