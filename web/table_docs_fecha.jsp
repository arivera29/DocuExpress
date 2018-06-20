<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.ControladorNoConformidad"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String oficina = (String) request.getParameter("oficina");
    String fecha1 = (String) request.getParameter("fecha1");
    String fecha2 = (String) request.getParameter("fecha2");
    String tfecha = (String) request.getParameter("tfecha");
    String tipoDoc = (String) request.getParameter("tipoDoc");

    db conexion = new db();
    String sql = "SELECT d.*, a.fecha, o.nomOficina, t.nomTipoDocumento "
            + "FROM documentos d "
            + " INNER JOIN tipoDocumento t ON t.codTipoDocumento = d.nomDocumento "
            + " INNER JOIN archivos a ON a.id = d.idCarga "
            + " INNER JOIN oficina o ON o.codOficina = a.oficina ";

    if (tfecha.equals("1")) {
        sql += "WHERE CONVERT(date,d.fechaCarga) BETWEEN CONVERT(date,?) AND CONVERT(date,?) ";
    }

    if (!oficina.equals("all")) {
        sql += " AND codOficina= '" + oficina + "'";
    }

    if (!tipoDoc.equals("all")) {
        sql += " AND d.nomDocumento = '" + tipoDoc + "' ";
    }

   


    sql += " ORDER BY a.fecha,nomOficina";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, fecha1);
    pst.setString(2, fecha2);

    java.sql.ResultSet rs = conexion.Query(pst);
    int contador = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ControladorNoConformidad controlador = new ControladorNoConformidad(conexion);

%>
<p>

</p>
<table class="table table-condensed">
     <thead>
        <tr>
            <th>TIPO</th>
            <th>NIC</th>
            <th>NRO. DOC</th>
            <th>MUNICIPIO</th>
            <th>DIRECCION</th>
            <th>BARRIO</th>
            <th>RECLAMANTE</th>
            <th>PROCESO</th>
            <th>OFICINA</th>
            <th>FECHA</th>
        </tr>
     </thead>
     <tbody>
     <% while (rs.next()) { %>
         <tr>
             <td>
                <% if (controlador.CantidadNoConformidad(rs.getInt("id"), 2) > 0) {  %>
                    <img src="images/notificacion.png">
                <% } %>
                 <%= rs.getString("nomTipoDocumento")  %>
             </td>
             <td><%= rs.getString("nic")  %></td>
             <td><%= rs.getString("nroDocumento")  %></td>
             <td><%= rs.getString("municipio")  %></td>
             <td><%= rs.getString("via")  %> <%= rs.getString("crucero")  %> <%= rs.getString("placa")  %></td>
             <td><%= rs.getString("barrio")  %></td>
             <td><%= rs.getString("nomReclamante")  %></td>
             <td><%= rs.getString("numProceso")  %></td>
             <td><%= rs.getString("nomOficina")  %></td>
             <td><%= sdf.format(rs.getDate("fechaCarga"))  %></td>
         </tr>
     <% }  %>
     </tbody>
 </table>
<div>
    <p>
        Total Guias <%= contador%>
    </p>
</div>

<% conexion.Close();%>