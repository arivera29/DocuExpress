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
    String sql = "SELECT archivo_impresion.*, tipoArchivo.nomTipoArchivo "
            + " FROM archivo_impresion "
            + " INNER JOIN tipoArchivo ON codTipoArchivo = tipo"
            + " WHERE id_archivo = ? and estado=1"
            + " ORDER BY fecha";
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
            <th>ARCHIVO</th>
            <th>SIZE</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <tr>
        <td><%= (String)rs.getString("fecha") %></td>
        <td><%= (String)rs.getString("usuario") %></td>
        <td><%= (String)rs.getString("nomTipoArchivo") %></td>
        <td><%= (String)rs.getString("local_filename") %></td>
        <td><%= Math.round((double)rs.getLong("size")/1026) %> KB</td>
        <td>
            <button type="button" onclick="javascript:eliminar(<%= rs.getInt("id") %>,<%= (String)request.getParameter("id") %>);" class="btn btn-primary btn-sm">Eliminar</button>
            <a href="SrvDownloadFile?contador=false&id=<%= rs.getInt("id") %>" class="btn btn-primary btn-sm">Descargar</a>
        </td>
    </tr>
    <% }  %>
    </tbody>
</table>

<% conexion.Close(); %>