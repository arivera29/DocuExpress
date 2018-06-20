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
    String tfecha = (String) request.getParameter("tfecha");
    String tipoDoc = (String) request.getParameter("tipoDoc");
    String tipo = "0";
    if (request.getParameter("tipo") != null) {
        tipo = (String) request.getParameter("tipo");
    }

    db conexion = new db();
    String sql = "SELECT g.*, a.fecha, o.nomOficina, d.nic, d.numProceso, t.nomTipoDocumento "
            + "FROM guias g "
            + " INNER JOIN documentos d ON d.id = g.idDocumento "
            + " INNER JOIN tipoDocumento t ON t.codTipoDocumento = d.nomDocumento "
            + " INNER JOIN archivos a ON a.id = d.idCarga "
            + " INNER JOIN oficina o ON o.codOficina = a.oficina ";

    if (tipo.equals("1")) {
        sql += "WHERE CONVERT(date,g.fechaUploadImagen) BETWEEN CONVERT(date,?) AND CONVERT(date,?) ";
        sql += "AND g.uploadImagen= 1";
    } else {
        if (tfecha.equals("1")) {
            sql += "WHERE CONVERT(date,d.fechaCarga) BETWEEN CONVERT(date,?) AND CONVERT(date,?) ";
        }
        if (tfecha.equals("2")) {
            sql += "WHERE CONVERT(date,g.fechaCarga) BETWEEN CONVERT(date,?) AND CONVERT(date,?) ";
        }
        if (tfecha.equals("3")) {
            sql += "WHERE CONVERT(date,g.fechaGestion) BETWEEN CONVERT(date,?) AND CONVERT(date,?) ";
        }
        if (tfecha.equals("4")) {
            sql += "WHERE CONVERT(date,g.fechaUploadImagen) BETWEEN CONVERT(date,?) AND CONVERT(date,?) ";
        }
    }

    if (!oficina.equals("all")) {
        sql += " AND codOficina= '" + oficina + "'";
    }

    if (!tipoDoc.equals("all")) {
        sql += " AND d.nomDocumento = '" + tipoDoc + "' ";
    }

    if (!estado.equals("all")) {
        sql += " AND g.estadoGuia = " + estado;
    } /* else {
     sql += " AND g.estadoGuia IN(2,3) ";
     }*/


    sql += " ORDER BY a.fecha,nomOficina";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, fecha1);
    pst.setString(2, fecha2);

    java.sql.ResultSet rs = conexion.Query(pst);
    int contador = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

%>
<p>

</p>
<table class="table table-condensed" id="tabla">
    <thead>
        <tr>
            <th>NRO. GUIA</th>
            <th>OFICINA</th>
            <th>NIC</th>
            <th>TIPO DOC.</th>
            <th>F.CARGA DOC</th>
            <th>F.CARGA GUIA</th>
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
            <td><a href="ver_documento.jsp?id=<%= rs.getString("idDocumento")%>" target="_blank"><%= rs.getString("nic")%></a></td>
            <td><%= rs.getString("nomTipoDocumento")%></td>
            <td><%= sdf.format(new java.util.Date(rs.getDate("fecha").getTime()))%></td>
            <td><%= sdf.format(new java.util.Date(rs.getDate("fechaCarga").getTime()))%></td>
            <td>
                <% if (rs.getInt("estadoGuia") == 1) {%>
                <img src="images/offline.png" title="<%= rs.getInt("estadoGuia")%>">
                <%  } %>
                <% if (rs.getInt("estadoGuia") == 2) {%>
                <img src="images/ok.gif" title="<%= rs.getInt("estadoGuia")%>">
                <%  } %>
                <% if (rs.getInt("estadoGuia") == 3) {%>
                <img src="images/warning.jpg" title="<%= rs.getInt("estadoGuia")%>">
                <%  }%>
            </td>
            <td>
                <% if (rs.getDate("fechaGestion") != null) {
                        out.print(sdf.format(new java.util.Date(rs.getDate("fechaGestion").getTime())));
                    } else {
                        out.print("NA");
                    }
                %>
            </td>
            <td><%
                if (rs.getString("usuarioGestion") != null) {
                    out.print(rs.getString("usuarioGestion"));
                } else {
                    out.print("NA");
                }
                %>
            </td>
            <td>
                <% if (rs.getInt("estadoGuia") == 3) {%>
                <%= rs.getString("causalDevolucion")%>
                <%  }  %>
            </td>
            <td>
                <% if (rs.getInt("uploadImagen") == 1) {%>
                <a href="SrvDownloadGuia?id=<%= rs.getString("id")%>"><img src="images/download.png" title="Descargar Guia"></a>
                    <% if (rs.getInt("confirmacion") == 1) { %>
                <img src="images/confirm.png" title="Guia Confirmada">
                <% } else { %>
                <img src="images/alerta.png" title="Guia No Confirmada">
                <% } %>
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