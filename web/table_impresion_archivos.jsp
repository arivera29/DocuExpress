<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String oficina = (String)request.getParameter("oficina");
    String empresa = (String)request.getParameter("empresa");
    String delegacion = (String)request.getParameter("delegacion");
    String departamento = (String)request.getParameter("departamento");
    String fecha1 = (String)request.getParameter("fecha1");
    String fecha2 = (String)request.getParameter("fecha2");
    
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    java.util.Date f1 = df.parse(fecha1);
    java.util.Date f2 = df.parse(fecha2);
    
    
    db conexion = new db();
    String sql = "SELECT id,usuario,fecha,estado,empresa,nomEmpresa,oficina,nomOficina,confirmar, "
            + "(SELECT count(*) FROM documentos WHERE idCarga=archivos.id) as Total, "
            + " dbo.ContadorArchivos(archivos.id) as cntPDF, "
            + " DATEDIFF(minute,fecha,SYSDATETIME()) tiempo "
            + " FROM archivos"
            + " INNER JOIN oficina ON oficina=codOficina "
            + " INNER JOIN empresa ON empresa=codEmpresa "
            + " WHERE  CONVERT(datetime,fecha) BETWEEN CONVERT(datetime,?) AND CONVERT(datetime,?) ";
    
    if (!oficina.equals("all")) {
        sql += " AND oficina= '" + oficina + "'";
    }
    
    if (!empresa.equals("all")) {
        sql += " AND empresa= '" + empresa + "'";
    }
    
    if (!delegacion.equals("all")) {
        sql += " AND idDelegacion = '" + delegacion + "'";
    }
    
    if (!departamento.equals("all")) {
        sql += " AND idDepartamento = '" + departamento + "'";
    }
    
               
    sql += " ORDER BY fecha DESC";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    //pst.setDate(1, new java.sql.Date(f1.getTime()));
    //pst.setDate(2, new java.sql.Date(f2.getTime()));
    pst.setString(1, fecha1);
    pst.setString(2, fecha2);
    java.sql.ResultSet rs = conexion.Query(pst);
    
    String clase_confirmado = "style=\"background-color : #20D7CF\"";
    String usuario = (String)session.getAttribute("usuario");
    String perfil  = (String)session.getAttribute("perfil");
%>
<p>
    <strong>Rango de fecha de la consulta: </strong> Inicial: <%= fecha1 %> Final: <%= fecha2 %>
</p>
<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th></th>
            <th>ID</th>
            <th>FECHA</th>
            <th>USUARIO</th>
            <th>OFICINA</th>
            <th>EMPRESA</th>
            <th>DOC.</th>
            <th>PDF</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <% while (rs.next()) { %>
    <%  if ( rs.getLong("Total") > 0) { %>
    <tr id="row_<%= rs.getInt("id")  %>" <%= (rs.getInt("confirmar")==1)?clase_confirmado:""   %> >
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
            <% if (rs.getInt("tiempo") > 15 || perfil.equals("1")) {  %>
                <a href="detalle_documentos_impresion.jsp?id=<%= rs.getInt("id") %>" target="_blank"><img src="images/view.png" title="Ver documentos" width="24px" height="24px"></a>

                <% if (rs.getInt("confirmar") == 0) { %>
                <a href="javascript:confirmar(<%= rs.getInt("id") %>);" ><img src="images/print.png" title="Confirmar impresión documentos"></a>
                <% } %>
            
            <% } else {  %>
                <strong style="color: red">Pend.Rev.</strong>
            <% }  %>
        </td>
    </tr>
    <% }  %>
    <% }  %>
    </tbody>
</table>

<% conexion.Close(); %>