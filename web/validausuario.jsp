<%
	if (session.getAttribute("usuario")== null) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
