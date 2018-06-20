<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<%    db conexion = new db();
    ControladorOficina c1 = new ControladorOficina(conexion);
    ArrayList<Oficina> lista1 = c1.list();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmar Guias</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <script>
            function list() {
                var oficina = $("#oficina").val();
                var estado = $("#estado").val();
                $("#lista").html("<img src='images/loading.gif'>");
                var url = "table_confirmar_guias.jsp";
                $("#lista").load(url, {
                    oficina: oficina,
                    estado: estado
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
            <h3>Confirmar Guias</h3>
            <hr>
            <form action="" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="oficina">Oficina Comercial</label>
                            <select name="oficina" id="oficina" class="form-control input-sm">
                                <option value="all">Todas</option>
                                <% for (Oficina oficina : lista1) {%>
                                <option value="<%= oficina.getCodigo()%>"><%= oficina.getNombre()%></option>
                                <% }%>
                            </select>

                        </div>
                        <div class="col-xs-2">
                            <label for="estado">Estado</label>
                            <select class="form-control input-sm" name="estado" id="estado">
                                <option value="all">Todos</option>
                                <option value="2">Entrega</option>
                                <option value="3">Devolucion</option>
                                
                            </select>
                        </div>

                    </div>


                </div>
                <div class="form-group">
                    <button type="button" onclick="javascript:list();" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Consultar</button>
                </div>
            </form>
            <div id="lista"></div>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>

</html>
<%
    conexion.Close();
%>
