<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String oficina = (String)request.getParameter("oficina");
    String empresa = (String)request.getParameter("empresa");
    String fecha1 = (String)request.getParameter("fecha1");
    String fecha2 = (String)request.getParameter("fecha2");
    
    db conexion = new db();
    String sql = "SELECT id,usuario,fecha,estado,empresa,nomEmpresa,oficina,nomOficina,confirmar, "
            + "(SELECT count(*) FROM documentos WHERE idCarga=archivos.id) as Total, "
            + " dbo.ContadorArchivos(archivos.id) as cntPDF, "
            + " DATEDIFF(minute,fecha,SYSDATETIME()) tiempo "
            + " FROM archivos,oficina,empresa "
            + " WHERE empresa=codEmpresa "
            + " AND CONVERT(date,fecha) BETWEEN CONVERT(date,?) AND CONVERT(date,?)"
            + " AND oficina=codOficina ";
    
    if (!oficina.equals("all")) {
        sql += " AND oficina= '" + oficina + "'";
    }
    
    if (!empresa.equals("all")) {
        sql += " AND empresa= '" + empresa + "'";
    }
    
               
    sql += " ORDER BY fecha DESC";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, fecha1);
    pst.setString(2, fecha2);
    java.sql.ResultSet rs = conexion.Query(pst);
    
    String usuario = (String)session.getAttribute("usuario");
    String perfil  = (String)session.getAttribute("perfil");
    
%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th></th>
            <th>ID</th>
            <th>FECHA</th>
            <th>USUARIO</th>
            <th>OFICINA</th>
            <th>EMPRESA</th>
            <th>DOC</th>
            <th>PDF</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <% if ( rs.getLong("total") > 0 ) { %>
    <tr id="fila_<%= rs.getString("id") %>">
        <td>
            <% if (rs.getInt("confirmar") == 1) { %>
            <img src="images/printer.png" title="Arhivo impreso">
            <% }  %>
        </td>
        <td><%= (String)rs.getString("id") %></td>
        <td><%= (String)rs.getString("fecha") %></td>
        <td><%= (String)rs.getString("usuario") %></td>
        <td><%= (String)rs.getString("nomOficina") %></td>
        <td><%= (String)rs.getString("nomEmpresa") %></td>
        <td><%= rs.getLong("total") %></td>
        <td><%
                    if (rs.getLong("cntPDF") == 0) {
                        out.print("<img src='images/warning.jpg' title='Sin documentos PDF'>");
                    }else {
                        out.print(rs.getLong("cntPDF"));
                    }
                %>
        </td>
        <td>
            <a href="detalle_documentos.jsp?id=<%= rs.getInt("id") %>" target="_blank"><img src="images/view.png" width="24px" height="24px" title="Ver Documentos"></a>
            <% if (perfil.equals("1") || (perfil.equals("2") && rs.getInt("tiempo")<= 15)) {  %>
            <a href="javascript:eliminar(<%= rs.getInt("id")%>,'fila_<%= rs.getString("id") %>');"><img src="images/remove.png" title="Eliminar Plantilla"></a>
            <% }  %>
        </td>
    </tr>
    <% }  %>
    <% }  %>
    </tbody>
</table>

<% conexion.Close(); %>