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
            + "(	SELECT count(archivos.id) "
            + "	FROM archivos "
            + " WHERE archivos.oficina= oficina.codOficina "
            + String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    if (!tipo.equals("all")) {
        sql += String.format(" AND archivos.id IN (SELECT DISTINCT documentos.idCarga FROM documentos WHERE archivos.id = documentos.idCarga AND documentos.nomDocumento = '%s')", tipo);
    }

    sql += ") AS total, "
            + "("
            + " SELECT count(*) "
            + " FROM documentos "
            + " INNER JOIN archivos on archivos.id = documentos.idCarga "
            + " WHERE archivos.oficina= oficina.codOficina "
            + String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    if (!tipo.equals("all")) {
        sql += String.format(" AND documentos.nomDocumento = '%s'", tipo);
    }
    sql += " ) AS cntArchivos,"
            + "( "
            + " SELECT count(*) "
            + " FROM archivo_impresion"
            + " INNER JOIN archivos ON archivos.id = archivo_impresion.id_archivo"
            + " WHERE archivos.oficina= oficina.codOficina"
            + String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    if (!tipo.equals("all")) {
        sql += String.format(" AND archivos.id IN (SELECT DISTINCT documentos.idCarga FROM documentos WHERE archivos.id = documentos.idCarga AND documentos.nomDocumento = '%s')", tipo);
    }
    sql += " ) AS cntPDF "
            + " FROM oficina "
            + " INNER JOIN departamento  ON codDepartamento = idDepartamento ";

    if (!oficina.equals("all")) {
        sql += " AND codOficina= '" + oficina + "'";
    }

    sql += " ORDER BY nomDepartamento,nomOficina";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

    java.sql.ResultSet rs = conexion.Query(pst);
    int contador = 0;

%>
<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th>DEPARTAMENTO</th>
            <th>CODIGO</th>
            <th>NOMBRE</th>
            <th>TOTAL ARCH.</th>
            <th>TOTAL DOC.</th>
            <th>TOTAL PDF</th>
        </tr>
    </thead>
    <tbody>
        <% while (rs.next()) {%>
        <tr>
            <td><%= (String) rs.getString("nomDepartamento")%></td>
            <td><%= (String) rs.getString("codOficina")%></td>
            <td><%= (String) rs.getString("nomOficina")%></td>
            <td><%= (String) rs.getString("total")%></td>
            <td><%= (String) rs.getString("cntArchivos")%></td>
            <td><%= (String) rs.getString("cntPDF")%></td>
        </tr>
        <% contador++; %>
        <% }%>
    </tbody>
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