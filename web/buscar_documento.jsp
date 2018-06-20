<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String criterio = (String)request.getParameter("criterio");
    criterio = "%" + criterio + "%";
    db conexion = new db();
    String sql = "SELECT d.*, a.fecha, o.nomOficina, a.confirmar "
            + " FROM documentos d "
            + " INNER JOIN archivos a ON a.id = d.idCarga "
            + " INNER JOIN oficina o ON a.oficina = o.codOficina "
            + " WHERE nic LIKE ? "
            + " OR nroDocumento LIKE ? "
            + " OR numProceso LIKE ? "
            + " OR codDocumento LIKE ? "
            + " ORDER BY fecha";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, criterio );
    pst.setString(2, criterio );
    pst.setString(3, criterio );
    pst.setString(4, criterio );
    java.sql.ResultSet rs = conexion.Query(pst);
    
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy H:m");
    
%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th></th>
            <th>NIC</th>
            <th>TIPO DOC.</th>
            <th>NUM. DOC.</th>
            <th>NUM. PROC.</th>
            <th>IDENT.</th>
            <th>RECLAMANTE</th>
            <th>DIRECCION</th>
            <th>OFIC.</th>
            <th>FECHA</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <tr>
        <td>
            <% if(rs.getInt("confirmar") == 1) { %>
            <img src="images/printer.png" alt="Impreso" />
            <% } %>
        </td>
        <td><%= (String)rs.getString("nic") %></td>
        <td><%= (String)rs.getString("nomDocumento") %></td>
        <td><%= (String)rs.getString("nroDocumento") %></td>
        <td><%= (String)rs.getString("numProceso") %></td>
        <td><%= (String)rs.getString("codDocumento") %></td>
        <td><%= (String)rs.getString("nomReclamante") %></td>
        <td><%= (String)rs.getString("via") %> <%= (String)rs.getString("crucero") %> <%= (String)rs.getString("placa") %> <%= (String)rs.getString("barrio") %> <%= (String)rs.getString("municipio") %></td>
        <td><%= (String)rs.getString("nomOficina") %></td>
        <td><%= sp.format(new java.util.Date(rs.getTimestamp("fecha").getTime())) %></td>
        <td>
            <a href="ver_documento.jsp?id=<%= rs.getInt("id") %>" class="btn btn-primary btn-sm" target="_blank">Detalle</a>
        </td>
    </tr>
    <% }  %>
    </tbody>
</table>

<% conexion.Close(); %>