<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String oficina = (String) request.getParameter("oficina");
    String fecha1 = (String) request.getParameter("fecha1");
    String fecha2 = (String) request.getParameter("fecha2");
    String tipo = (String) request.getParameter("tipo");

    db conexion = new db();
    String sql = "SELECT nomDepartamento,codOficina, nomOficina,"
            + "("
            + " SELECT count(*)"
            + " FROM documentos "
            + " INNER JOIN archivos ON archivos.id = documentos.idCarga "
            + " WHERE  archivos.oficina = oficina.codOficina "
            + String.format(" AND CONVERT(DATE,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s') ", fecha1, fecha2);
    if (!tipo.equals("all")) {
        sql += String.format(" AND documentos.nomDocumento = '%s' ", tipo);
    }
    sql += " ) AS cntDocs, "
            + " ( "
            + " SELECT count(*) "
            + " FROM guias "
            + " INNER JOIN documentos ON documentos.id = guias.idDocumento "
            + " INNER JOIN archivos ON archivos.id = documentos.idCarga "
            + " WHERE  archivos.oficina = oficina.codOficina "
            + String.format(" AND CONVERT(DATE,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s') ", fecha1, fecha2);
    if (!tipo.equals("all")) {
        sql += String.format(" AND documentos.nomDocumento = '%s' ", tipo);
    }
    sql += " ) AS cntGuias, "
            + " ( "
            + " SELECT count(*) "
            + " FROM guias "
            + " INNER JOIN documentos ON documentos.id = guias.idDocumento "
            + " INNER JOIN archivos ON archivos.id = documentos.idCarga "
            + " WHERE  archivos.oficina = oficina.codOficina "
            + String.format(" AND CONVERT(DATE,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s') ", fecha1, fecha2)
            + " AND guias.estadoGuia != 1 ";
    if (!tipo.equals("all")) {
        sql += String.format(" AND documentos.nomDocumento = '%s' ", tipo);
    }
    sql += " ) as cntGestion, "
            + " ( "
            + " SELECT count(*) "
            + " FROM guias "
            + " INNER JOIN documentos ON documentos.id = guias.idDocumento "
            + " INNER JOIN archivos ON archivos.id = documentos.idCarga "
            + " WHERE  archivos.oficina = oficina.codOficina "
            + String.format(" AND CONVERT(DATE,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s') ", fecha1, fecha2)
            + " AND guias.uploadImagen = 1 ";
    if (!tipo.equals("all")) {
        sql += String.format(" AND documentos.nomDocumento = '%s' ", tipo);
    }
    sql += " ) AS cntImagenes  "
            + " FROM oficina "
            + " INNER JOIN departamento  ON codDepartamento = idDepartamento ";

    if (!oficina.equals("all")) {
        sql += " AND codOficina= '" + oficina + "'";
    }

    sql += " ORDER BY nomDepartamento,nomOficina";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

    java.sql.ResultSet rs = conexion.Query(pst);
    int contador = 0;
    long suma1 = 0, suma2 = 0, suma3 = 0, suma4 = 0;

%>

<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th>DEPARTAMENTO</th>
            <th>CODIGO</th>
            <th>NOMBRE</th>
            <th>TOTAL DOC.</th>
            <th>TOTAL GUIAS</th>
            <th>TOTAL GESTION</th>
            <th>TOTAL IMAGEN</th>
        </tr>
    </thead>
    <tbody>
        <% while (rs.next()) {%>
        <tr>
            <td><%= (String) rs.getString("nomDepartamento")%></td>
            <td><%= (String) rs.getString("codOficina")%></td>
            <td><%= (String) rs.getString("nomOficina")%></td>
            <td><%= (String) rs.getString("cntDocs")%></td>
            <td><%= (String) rs.getString("cntGuias")%></td>
            <td><%= (String) rs.getString("cntGestion")%></td>
            <td><%= (String) rs.getString("cntImagenes")%></td>
        </tr>
        <%

            contador++;
            suma1 += rs.getInt("cntDocs");
            suma2 += rs.getInt("cntGuias");
            suma3 += rs.getInt("cntGestion");
            suma4 += rs.getInt("cntImagenes");
        %>
        <% }%>
    </tbody>
    <tfoot>
        <tr>
            <th colspan="3">TOTALES</th>
            <th><%= suma1%></th>
            <th><%= suma2%></th>
            <th><%= suma3%></th>
            <th><%= suma4%></th>

        </tr>
    </tfoot>
</table>
<div>
    <p>
        Total Oficinas: <%= contador%>
    </p>
</div>


<form name="_form2" id="_form2" method="post" action="ExportarExcel.jsp">
    <p>
        <button type="button" onclick="javascript:exportar()" class="btn btn-primary btn-sm">Exportar Excel</button>
        <input type="hidden" id="data" name="data" />     
    </p>
</form>
<script>
    function exportar() {
        $("#data").val($("<table>").append($("#tabla").eq(0).clone()).html());
        $("#_form2").submit();
    }
</script>

<% conexion.Close();%>