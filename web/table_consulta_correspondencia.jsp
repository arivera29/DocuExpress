<%-- 
    Document   : table_archivos
    Created on : 28-ago-2016, 7:23:30
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String oficina = (String) request.getParameter("oficina");
    String delegacion = (String) request.getParameter("delegacion");
    String departamento = (String) request.getParameter("departamento");
    String fecha1 = (String) request.getParameter("fecha1");
    String fecha2 = (String) request.getParameter("fecha2");
    String tipo = (String) request.getParameter("tipo");
    String tipoFecha = (String) request.getParameter("tipoFecha");

    db conexion = new db();
    String sql = "SELECT nomDepartamento,nomDelegacion, nomOficina,"
            + "(	SELECT count(archivos.id) "
            + "	FROM archivos "
            + " WHERE archivos.oficina= oficina.codOficina AND archivos.estado = 1 AND (SELECT count(*) FROM documentos WHERE idCarga=archivos.id) > 0 ";
    if (tipoFecha.equals("1")) {
        sql += String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    } else {
        sql += String.format(" AND CONVERT(date,archivos.fecha_impresion) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    }
    if (!tipo.equals("all")) {
        sql += String.format(" AND archivos.id IN (SELECT DISTINCT documentos.idCarga FROM documentos WHERE archivos.id = documentos.idCarga AND documentos.nomDocumento = '%s')", tipo);
    }

    sql += ") AS total, "
            + "("
            + " SELECT count(*) "
            + " FROM documentos "
            + " INNER JOIN archivos on archivos.id = documentos.idCarga "
            + " WHERE archivos.oficina= oficina.codOficina AND archivos.estado = 1 ";
    if (tipoFecha.equals("1")) {
        sql += String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    } else {
        sql += String.format(" AND CONVERT(date,archivos.fecha_impresion) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    }
    if (!tipo.equals("all")) {
        sql += String.format(" AND documentos.nomDocumento = '%s'", tipo);
    }
    sql += " ) AS cntArchivos,"
            + "( "
            + " SELECT count(*) "
            + " FROM archivo_impresion"
            + " INNER JOIN archivos ON archivos.id = archivo_impresion.id_archivo"
            + " WHERE archivos.oficina= oficina.codOficina AND archivos.estado = 1";
    if (tipoFecha.equals("1")) {
        sql += String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    } else {
        sql += String.format(" AND CONVERT(date,archivos.fecha_impresion) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    }
    if (!tipo.equals("all")) {
        sql += String.format(" AND archivos.id IN (SELECT DISTINCT documentos.idCarga FROM documentos WHERE archivos.id = documentos.idCarga AND documentos.nomDocumento = '%s')", tipo);
    }
    sql += " ) AS cntPDF, ";

    sql += "( "
            + " SELECT count(*) "
            + " FROM archivos "
            + " WHERE archivos.oficina = oficina.codOficina AND archivos.estado = 1 "
            + " AND archivos.confirmar = 1 ";
    if (tipoFecha.equals("1")) {
        sql += String.format(" AND CONVERT(date,archivos.fecha) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    } else {
        sql += String.format(" AND CONVERT(date,archivos.fecha_impresion) BETWEEN CONVERT(date,'%s') AND CONVERT(date,'%s')", fecha1, fecha2);
    }
    if (!tipo.equals("all")) {
        sql += String.format(" AND archivos.id IN (SELECT DISTINCT documentos.idCarga FROM documentos WHERE archivos.id = documentos.idCarga AND documentos.nomDocumento = '%s')", tipo);
    }
    sql += " ) AS cntImpresion "
            + " FROM oficina "
            + " INNER JOIN departamento  ON codDepartamento = idDepartamento "
            + " INNER JOIN delegacion  ON codDelegacion = idDelegacion ";

    if (!oficina.equals("all")) {
        sql += " AND codOficina= '" + oficina + "'";
    }

    if (!delegacion.equals("all")) {
        sql += "AND oficina.idDelegacion = '" + delegacion + "' ";
    }

    if (!departamento.equals("all")) {
        sql += "AND oficina.idDepartamento = '" + departamento + "' ";
    }

    

    sql += " ORDER BY nomDepartamento,nomDelegacion,nomOficina";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

    java.sql.ResultSet rs = conexion.Query(pst);
    int contador = 0;

%>
<table class="table table-striped" id="tabla">
    <thead>
        <tr>
            <th>DEPARTAMENTO</th>
            <th>DELEGACION</th>
            <th>NOMBRE</th>
            <th>TOTAL ARCH.</th>
            <th>TOTAL DOC.</th>
            <th>TOTAL PDF</th>
            <th>TOTAL IMP.</th>
        </tr>
    </thead>
    <tbody>
        <% while (rs.next()) {%>
        <tr>
            <td><%= (String) rs.getString("nomDepartamento")%></td>
            <td><%= (String) rs.getString("nomDelegacion")%></td>
            <td><%= (String) rs.getString("nomOficina")%></td>
            <td><%= (String) rs.getString("total")%></td>
            <td><%= (String) rs.getString("cntArchivos")%></td>
            <td><%= (String) rs.getString("cntPDF")%></td>
            <td><%= (String) rs.getString("cntImpresion")%></td>
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