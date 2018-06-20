<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Buscar Documento</title>
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
            <h3>Buscar Documento</h3>
            <hr>
            <div id="info"></div>
            <form action="" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="criterio">Criterio</label>
                            <input type="text" class="form-control input-sm" name="criterio" id="criterio" placeholder="Ingrese NIC o Num. Proceso o Identificador documento">
                        </div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <button type="button" onclick="javascript:buscar();" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Consultar</button>
                    </div>
            </form>
            <div id="lista"></div>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>

</html>
