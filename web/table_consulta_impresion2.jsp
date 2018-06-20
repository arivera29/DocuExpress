<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.ControladorNoConformidad"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String id = (String)request.getParameter("id");
    db conexion = new db();
    String sql = "SELECT archivo_impresion.*, nomTipoArchivo, (SELECT count(*) FROM historial_descargas WHERE id_doc_impresion = archivo_impresion.id) AS cnt_download "
            + " FROM archivo_impresion "
            + " INNER JOIN tipoArchivo ON codTipoArchivo = tipo"
            + " WHERE id_archivo = ? and estado=1"
            + " ORDER BY fecha";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    java.sql.ResultSet rs = conexion.Query(pst);
    String usuario = (String)session.getAttribute("usuario");
    String perfil = (String)session.getAttribute("perfil");
    int contador=0;
    
    ControladorNoConformidad controlador = new ControladorNoConformidad(conexion);
%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th>FECHA</th>
            <th>USUARIO</th>
            <th>TIPO</th>
            <th>ARCHIVO</th>
            <th>SIZE</th>
            <th><img src="images/download.png" ></th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <tr>
        <td>
            <% if (controlador.CantidadNoConformidad(rs.getInt("id"), 1) > 0) {  %>
            <img src="images/notificacion.png">
            <% } %>
            <%= (String)rs.getString("fecha") %>
        </td>
        <td><%= (String)rs.getString("usuario") %></td>
        <td><%= (String)rs.getString("nomTipoArchivo") %></td>
        <td><%= (String)rs.getString("local_filename") %></td>
        <td><%= Math.round((double)rs.getLong("size")/1026) %> KB</td>
        <td><%= rs.getLong("cnt_download") %></td>
        <td>
            <a href="SrvDownloadFile?contador=true&id=<%= rs.getInt("id") %>"><img src="images/download.png" title="Descargar archivo"></a>
            <a href="no_conformidad.jsp?id=<%= rs.getInt("id") %>&tipo=1" target="_blank" title="No conformidad"><img src="images/notification.png" title="Levantar no conformidad" width="24px" height="24px"></a>
        </td>
    </tr>
    <% contador++; %>
    <% }  %>
    </tbody>
</table>
<%  if (contador > 0) { %>
<p>
    <a href="SrvDescargarAll?id=<%= (String)request.getParameter("id") %>" class="btn btn-primary">Descargar todos</a>
    <a href="SrvOrganicePdfTest1?id=<%= (String)request.getParameter("id") %>" class="btn btn-primary">Descargar Organizado</a>
    <% if (perfil.equals("1")) {  %>
<!--    <a href="SrvOrganicePdfTest1?id=<%= (String)request.getParameter("id") %>" class="btn btn-primary">Descargar Organizado Test</a>-->
    <% } %>
</p>
<% }  %>
<% conexion.Close(); %>