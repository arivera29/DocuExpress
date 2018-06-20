<%-- 
    Document   : listado_plantilla
    Created on : 07-sep-2016, 9:20:51
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.ControladorNoConformidad"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
 
    db conexion = new db();
    String id = (String)request.getParameter("id");
    String sql = "SELECT * FROM documentos INNER JOIN tipoDocumento ON nomDocumento = codTipoDocumento WHERE idCarga = ? ORDER BY id";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    java.sql.ResultSet rs = conexion.Query(pst);
    ControladorNoConformidad controlador = new ControladorNoConformidad(conexion);
    
 %>
 
 <table class="table table-condensed">
     <thead>
        <tr>
            <th>TIPO</th>
            <th>NIC</th>
            <th>NRO. DOC</th>
            <th>NUM. PROCESO</th>
            <th>MUNICIPIO</th>
            <th>DIRECCION</th>
            <th>BARRIO</th>
            <th>RECLAMANTE</th>
            <th>TELEFONO</th>
            <th></th>
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
             <td><%= rs.getString("numProceso")  %></td>
             <td><%= rs.getString("municipio")  %></td>
             <td><%= rs.getString("via")  %> <%= rs.getString("crucero")  %> <%= rs.getString("placa")  %></td>
             <td><%= rs.getString("barrio")  %></td>
             <td><%= rs.getString("nomReclamante")  %></td>
             <td><%= rs.getString("telefono")  %></td>
             <td><a href="no_conformidad.jsp?id=<%= rs.getInt("id") %>&tipo=2" target="_blank"><img src="images/notification.png" title="Levantar no conformidad" width="24px" height="24px"></a></td>
         </tr>
     <% }  %>
     </tbody>
 </table>
 
 
 
 <% 
    conexion.Close();
 %>