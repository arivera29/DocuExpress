<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<%    db conexion = new db();
    String id = (String) request.getParameter("id");
    String sql = "SELECT d.*, a.fecha, o.nomOficina, a.confirmar "
            + " FROM documentos d "
            + " INNER JOIN archivos a ON a.id = d.idCarga "
            + " INNER JOIN oficina o ON a.oficina = o.codOficina "
            + " WHERE d.id = ? ";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);

    java.sql.ResultSet rs = conexion.Query(pst);
    boolean rsIsEmpty = !rs.next();

    sql = "SELECT g.* "
            + "FROM guias g "
            + "WHERE g.idDocumento = ? "
            + "ORDER BY fechaCarga";
    pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    
    java.sql.ResultSet rsGuias = conexion.Query(pst);
    int contador=0;

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Documento</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <script>
            function buscar() {
                var criterio = $("#criterio").val();
                if (criterio == "") {
                    $("#info").html("Debe ingresar el criterio de busqueda");
                    return;
                }


                var url = "buscar_documento.jsp";
                $("#lista").load(url, {
                    criterio: criterio
                }, function () {
                    $(".table").bootstrapTable({
                        pagination: true,
                        paginationLoop: false,
                        search: true
                    });
                });
            }


        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3>Documento</h3>
            <table class="table">
                <tbody>
                <tr>
                    <th>NIC</th>
                    <td><%= rs.getString("nic")%></td>
                    <th>TIPO DOCUMENTO</th>
                    <td><%= rs.getString("nomDocumento")%></td>
                </tr>
                <tr>
                    <th>NRO. DOCUMENTO</th>
                    <td><%= rs.getString("nroDocumento")%></td>
                    <th>NRO. PROCESO</th>
                    <td><%= rs.getString("numProceso")%></td>
                </tr>
                <tr>
                    <th>RECLAMANTE</th>
                    <td><%= rs.getString("nomReclamante")%></td>
                    <th>DIRECCION</th>
                    <td><%= (String) rs.getString("via")%> <%= (String) rs.getString("crucero")%> <%= (String) rs.getString("placa")%></td>
                </tr>
                <tr>
                    <th>BARRIO</th>
                    <td><%= rs.getString("barrio")%></td>
                    <th>MUNICIPIO</th>
                    <td><%= rs.getString("municipio")%></td>
                </tr>
                <tr>
                    <th>FECHA CARGA</th>
                    <td><%= rs.getString("fechaCarga")%></td>
                    <th>USUARIO CARGA</th>
                    <td><%= rs.getString("usuarioCarga")%></td>
                </tr>
                <tr>
                    <th>IDENTIFICADOR</th>
                    <td><%= rs.getString("id")%></td>
                    <th>FILA ARCHIVO FUENTE</th>
                    <td><%= rs.getString("rowFile")%></td>
                </tr>
                </tbody>
            </table>
            <h3>Guias</h3>
            <table class="table">
                <thead>
                <tr>
                    <th>NRO. GUIA</th>
                    <th>FECHA CARGA</th>
                    <th>ESTADO</th>
                    <th>FECHA GESTION</th>
                    <th>USUARIO GESTION</th>
                    <th>CAUSAL DEV.</th>
                    <th>ARCHIVO</th>
                </tr>
                </thead>
                <tbody>
                    <% while (rsGuias.next()) { %>
                    <tr>
                        <td><%= rsGuias.getString("numeroGuia") %></td>
                        <td><%= rsGuias.getString("fechaCarga") %></td>
                        <td>
                            <% if (rsGuias.getInt("estadoGuia") == 1) { %>
                            <img src="images/offline.png">
                            <%  } %>
                            <% if (rsGuias.getInt("estadoGuia") == 2) { %>
                            <img src="images/ok.gif">
                            <%  } %>
                            <% if (rsGuias.getInt("estadoGuia") == 3) { %>
                            <img src="images/warning.jpg">
                            <%  } %>
                        </td>
                        <td><%= rsGuias.getString("fechaGestion") %></td>
                        <td><%= rsGuias.getString("usuarioGestion") %></td>
                        <td>
                            <% if (rsGuias.getInt("estadoGuia") ==3) {  %>
                                <%= rsGuias.getString("causalDevolucion") %>
                            <%  }  %>
                        </td>
                        <td>
                            <% if (rsGuias.getInt("uploadImagen") == 1) { %>
                            <a href="SrvDownloadGuia?id=<%= rsGuias.getString("id") %>"><img src="images/download.png" alt="Descargar Guia"></a>
                            <% if (rsGuias.getInt("confirmacion") == 1) { %>
                                <img src="images/confirm.png" title="Guia Confirmada">
                            <% } else { %>
                                <img src="images/alerta.png" title="Guia No Confirmada">
                            <% } %>
                            <% }  %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>

</html>
<%
    conexion.Close();
%>
