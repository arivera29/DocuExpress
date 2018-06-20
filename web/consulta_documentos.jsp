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
    ControladorEmpresa c2 = new ControladorEmpresa(conexion);
    ArrayList<Empresa> lista2 = c2.List(1);

    SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = formateador.format(new Date());

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Subir Documentos</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
        <script>
            function list() {
                var oficina = $("#oficina").val();
                var empresa = $("#empresa").val();
                var fecha1 = $("#fecha1").val();
                var fecha2 = $("#fecha2").val();

                $("#lista").html("<img src='images/loading.gif'> Consultando...");
                var url = "table_consulta_archivos.jsp";
                $("#lista").load(url, {
                    oficina: oficina,
                    empresa: empresa,
                    fecha1: fecha1,
                    fecha2: fecha2
                }, function () {
                    $(".table").bootstrapTable({
                        pagination: true,
                        paginationLoop: false,
                        search: true
                    });
                });
            }

            function eliminar(id, fila) {
                if (confirm("Está seguro de eliminar la plantilla y sus documentos adjuntos?")) {
                    var url = "SrvDeletePlantilla";
                    $.post(url, {
                        id: id
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#" + fila).remove().fadeIn(100);
                            $("#info").html("<img src='images/ok.gif'> Archivo eliminado correctamente ");
                        } else {
                            $("#info").html("<img src='images/warning.jpg'> " + data);

                        }
                    });

                }

            }


        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3>Consulta Documentos</h3>
            <hr>
            <form action="SrvDownloadDocumentos" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="oficina">Oficina Comercial</label>
                            <select name="oficina" id="oficina" class="form-control input-sm">
                                <option value="all">Seleccionar</option>
                                <% for (Oficina oficina : lista1) {%>
                                <option value="<%= oficina.getCodigo()%>"><%= oficina.getNombre()%></option>
                                <% } %>
                            </select>

                        </div>
                        <div class="col-xs-3">
                            <label for="empresa">Empresa mensajeria</label>
                            <select name="empresa" id="empresa" class="form-control input-sm">
                                <option value="all">Seleccionar</option>
                                <% for (Empresa empresa : lista2) {%>
                                <option value="<%= empresa.getCodigo()%>"><%= empresa.getNombre()%></option>
                                <% }%>
                            </select>

                        </div>

                        <div class="col-xs-2">
                            <label for="fecha1">Fecha Inicial</label>
                            <div class="input-group date form_date" data-date="" data-date-format="yyyy-mm-dd" data-link-field="fecha1" data-link-format="yyyy-mm-dd" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha1" name="fecha1" value="<%= fecha%>"  />
                        </div>
                        <div class="col-xs-2">
                            <label for="fecha2">Fecha Final</label>
                            <div class="input-group date form_date" data-date="" data-date-format="yyyy-mm-dd" data-link-field="fecha2" data-link-format="yyyy-mm-dd" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha2" name="fecha2" value="<%= fecha%>"  />
                        </div>
                    </div>


                </div>
                <div class="form-group">
                    <button type="button" onclick="javascript:list();" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Consultar</button>
                    <button type="submit" name="cmd_descargar" id="cmd_descargar" class="btn btn-primary btn-sm">Descargar</button>
                </div>

                <div class="alert alert-success alert-dismissible" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <strong>Aviso!</strong>
                    <p>
                        Una vez termine el proceso de carga de la plantilla, el usuario dispone de 15 minutos para revisar la plantilla.
                        Después de este tiempo no se podrá eliminar la plantilla, se deberá informar al administrador del sistema.

                    </p>

                </div>
            </form>
            <div id="lista"></div>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="js/locales/bootstrap-datetimepicker.es.js" charset="UTF-8"></script>
    <script type="text/javascript">
                        $('.form_date').datetimepicker({
                            language: 'es',
                            format: 'yyyy-mm-dd',
                            weekStart: 1,
                            todayBtn: 1,
                            autoclose: 1,
                            todayHighlight: 1,
                            startView: 2,
                            minView: 2,
                            forceParse: 0
                        });
    </script>

</html>
<%
    conexion.Close();
%>
