<% 
	String nombre = "listado.xls"; 
	response.setHeader("Content-Disposition","attachment; filename=\""+ nombre + "\""); 
	String datos = (String)request.getParameter("data");
	out.write(datos);
%>  