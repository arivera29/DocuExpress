<%
	session.removeAttribute("usuario");
	session.removeAttribute("perfil");
	response.sendRedirect("index.jsp");
%>
