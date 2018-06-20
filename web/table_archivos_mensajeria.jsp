<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    db conexion = new db();
    String sql = "SELECT id,usuario,fecha,estado,empresa,nomEmpresa,oficina,nomOficina, (SELECT count(*) FROM documentos WHERE idCarga=archivos.id) as Total "
            + " FROM archivos,oficina,empresa "
            + " WHERE empresa=codEmpresa "
 //           + " AND CONVERT(date,fecha) = CONVERT(date,GETDATE())"
            + " AND oficina=codOficina"
            + " ORDER BY fecha DESC";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    java.sql.ResultSet rs = conexion.Query(pst);
    
%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th>FECHA</th>
            <th>USUARIO</th>
            <th>OFICINA</th>
            <th>EMPRESA</th>
            <th>DOC.</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <tr>
        <td><%= (String)rs.getString("fecha") %></td>
        <td><%= (String)rs.getString("usuario") %></td>
        <td><%= (String)rs.getString("nomOficina") %></td>
        <td><%= (String)rs.getString("nomEmpresa") %></td>
        <td><%= rs.getLong("total") %></td>
        <td>
            <a href="SrvDownloadPlantilla?id=<%= rs.getInt("id") %>" class="btn btn-primary btn-sm">Plantilla</a>
        </td>
    </tr>
    <% }  %>
    </tbody>
</table>

<% conexion.Close(); %>