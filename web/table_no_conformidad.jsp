<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String id = (String)request.getParameter("id");
    
    db conexion = new db();
    String sql = "SELECT n.*, t.nomTipoNoConformidad "
            + " FROM NoConformidad n "
            + " INNER JOIN TipoNOConformidad t ON n.ncTipoNc = t.codTipoNoConformidad "
            + " WHERE n.ncIdArchivo = ? ";
    
               
    sql += " ORDER BY n.ncfecha DESC";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    java.sql.ResultSet rs = conexion.Query(pst);
    
%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th>FECHA</th>
            <th>USUARIO</th>
            <th>TIPO</th>
            <th>OBSERVACION</th>
            <th>ESTADO</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <tr>
        <td><%= (String)rs.getString("ncFecha") %></td>
        <td><%= (String)rs.getString("ncUsuario") %></td>
        <td><%= (String)rs.getString("nomTipoNoConformidad") %></td>
        <td><%= (String)rs.getString("ncObservacion") %></td>
        <td>
                <%
                    switch (rs.getInt("ncEstado")) {
                        case 1:
                            out.print("ABIERTA");
                            break;
                        case 2:
                            out.print("CERRADA");
                            break;
                    }
                %>
        </td>
        <td>
            <a href="anotacion_no_conformidad.jsp?id=<%= rs.getInt("ncId") %>" class="btn btn-primary btn-sm">Anotaciones</a>
        </td>
    </tr>
    <% }  %>
    </tbody>
</table>

<% conexion.Close(); %>