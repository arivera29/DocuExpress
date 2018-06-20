<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String usuario = (String) session.getAttribute("usuario");
    String perfil = (String) session.getAttribute("perfil");

    db conexion = new db();

    String sql = "SELECT ";
    if (usuario.equals("admin") || perfil.equals("1")) {
        sql += " TOP(100)";
    }
    sql += " id,usuario,fecha,estado,empresa,nomEmpresa,oficina,nomOficina, "
            + "(SELECT count(*) FROM documentos WHERE idCarga=archivos.id) as Total,"
            + " dbo.ContadorArchivos(archivos.id) as cntPDF,"
            + " DATEDIFF(minute,fecha,SYSDATETIME()) tiempo "
            + " FROM archivos,oficina,empresa "
            + " WHERE empresa=codEmpresa "
            + " AND oficina=codOficina ";

    if (!usuario.equals("admin") && !perfil.equals("1")) {
        sql += " AND CONVERT(date,fecha) = CONVERT(date,GETDATE())";
        sql += " AND usuario ='" + usuario + "'";
    }

    sql += " ORDER BY fecha DESC ";
    
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    java.sql.ResultSet rs = conexion.Query(pst);

%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
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
        <% while (rs.next()) {%>
        <%  if (rs.getLong("total") > 0 ) { %>
        <tr id="fila_<%= rs.getString("id")  %>">
            <td><%= (String) rs.getString("id")%></td>
            <td><%= (String) rs.getString("fecha")%></td>
            <td><%= (String) rs.getString("usuario")%></td>
            <td><%= (String) rs.getString("nomOficina")%></td>
            <td><%= (String) rs.getString("nomEmpresa")%></td>
            <td><%= rs.getLong("total")%></td>
            <td><%
                    if (rs.getLong("cntPDF") == 0) {
                        out.print("<img src='images/warning.jpg' title='Sin documentos PDF'>");
                    }else {
                        out.print(rs.getLong("cntPDF"));
                    }
                %>
            </td>
            <td>
                <a href="ver_documentos.jsp?id=<%= rs.getInt("id")%>"><img src="images/view.png" title="Ver documentos" width="24px" height="24px"></a>
                <% if (rs.getInt("tiempo") <= 15 || perfil.equals("1")) {  %>
                <a href="javascript:eliminar(<%= rs.getInt("id")%>,'fila_<%= rs.getString("id") %>');"><img src="images/remove.png" title="Eliminar Plantilla"></a>
                <% }  %>
            </td>
        </tr>
        <% }  %>
        <% }  %>
    </tbody>
</table>

<% conexion.Close();%>