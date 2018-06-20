<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String oficina = (String) request.getParameter("oficina");
    String fecha1 = (String) request.getParameter("fecha1");
    String fecha2 = (String) request.getParameter("fecha2");
    String estado = (String) request.getParameter("estado");
    

    db conexion = new db();
    String sql = "SELECT TOP(100) g.*, o.nomOficina, d.nic, d.numProceso, d.nomDocumento "
            + "FROM guias g "
            + " INNER JOIN documentos d ON d.id = g.idDocumento "
            + " INNER JOIN archivos a ON a.id = d.idCarga "
            + " INNER JOIN oficina o ON o.codOficina = a.oficina "
            +  " WHERE g.uploadImagen= 1 "
            + " AND confirmacion = 0";
    

    if (!oficina.equals("all")) {
        sql += " AND codOficina= '" + oficina + "'";
    }

    if (!estado.equals("all")) {
        sql += " AND g.estadoGuia = " + estado;
    } else {
        sql += " AND g.estadoGuia IN(2,3) ";
    }

    sql += " ORDER BY fechaGestion,nomOficina";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    
    java.sql.ResultSet rs = conexion.Query(pst);
    int contador = 0;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

%>
<div class="alert alert-warning alert-dismissible" role="alert">
    <p>Esta consulta solo mostrará un máximo de 100 registros.</p>
</div>
<table class="table table-condensed">
    <thead>
        <tr>
            <th>NRO. GUIA</th>
            <th>OFICINA</th>
            <th>NIC</th>
            <th>TIPO DOC.</th>
            <th>FECHA CARGA</th>
            <th>ESTADO</th>
            <th>FECHA GESTION</th>
            <th>USUARIO GESTION</th>
            <th>CAUSAL DEV.</th>
            <th>ACCION</th>
        </tr>
    </thead>
    <tbody>
        <% while (rs.next()) {%>
        <tr>
            <td><%= rs.getString("numeroGuia")%></td>
            <td><%= rs.getString("nomOficina")%></td>
            <td><a href="ver_documento.jsp?id=<%= rs.getString("idDocumento") %>" target="_blank"><%= rs.getString("nic")%></a></td>
            <td><%= rs.getString("nomDocumento")%></td>
            <td><%= sdf.format(new java.util.Date(rs.getDate("fechaCarga").getTime()))%></td>
            <td>
                <% if (rs.getInt("estadoGuia") == 1) { %>
                <img src="images/offline.png" title="<%= rs.getInt("estadoGuia") %>">
                <%  } %>
                <% if (rs.getInt("estadoGuia") == 2) { %>
                    <img src="images/ok.gif" title="<%= rs.getInt("estadoGuia") %>">
                <%  } %>
                <% if (rs.getInt("estadoGuia") == 3) { %>
                <img src="images/warning.jpg" title="<%= rs.getInt("estadoGuia") %>">
                <%  }%>
            </td>
            <td><%= sdf.format(new java.util.Date(rs.getDate("fechaGestion").getTime())) %></td>
            <td><%= rs.getString("usuarioGestion")%></td>
            <td>
                <% if (rs.getInt("estadoGuia") == 3) {%>
                <%= rs.getString("causalDevolucion")%>
                <%  }  %>
            </td>
            <td>
                <% if (rs.getInt("uploadImagen") == 1) {%>
                <a href="SrvDownloadGuia?id=<%= rs.getString("id")%>"><img src="images/download.png" title="Descargar Guia"></a>
                <a href="javascript:confirmar(<%= rs.getString("id") %>)"><img src="images/confirm.png" title="Confirmar Guia"></a>
                <% }  %>
            </td>
        </tr>
        <% contador++; %>
        <% }%>
    </tbody>
</table>
<div>
    <p>
        Total Guias <%= contador%>
    </p>
</div>
<% conexion.Close();%>