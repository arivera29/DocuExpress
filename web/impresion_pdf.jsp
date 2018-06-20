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
    ControladorDelegacion c3 = new ControladorDelegacion(conexion);
    ArrayList<Delegacion> lista3 = c3.list();
    ControladorDepartamento c4 = new ControladorDepartamento(conexion);
    ArrayList<Departamento> lista4 = c4.list();

    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String fecha = formateador.format(new Date());

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestion Mensajeria</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
        <script>
            function filtrar() {
                $("#info").html("");
                var oficina = $("#oficina").val();
                var delegacion = $("#delegacion").val();
                var departamento = $("#departamento").val();
                var empresa = $("#empresa").val();
                var fecha1 = $("#fecha1").val();
                var fecha2 = $("#fecha2").val();

                if (empresa == "") {
                    $("#info").html("<img src='images/warning.jpg'> Debe seleccionar la empresa de mensajeria");
                    return;
                }
                $("#lista").html("<img src='images/loading.gif'> Consultando...");
                var url = "table_impresion_archivos.jsp";
                $("#lista").load(url, {
                    oficina: oficina,
                    delegacion: delegacion,
                    departamento: departamento,
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

            function confirmar(id) {
                if (confirm("Esta seguro de confirmar la impresión de los documentos?")) {
                    var url = "SrvConfirmar";
                    $.post(url,
                            {
                                id: id

                            }, function (data) {
                        if (data == "OK") {
                            alert("Documento confirmado Correctamente");
                            var rowId = "#row_" + id;
                            $(rowId).css("background-color", "#20D7CF");
                        } else {
                            alert(data);
                        }
                    }
                    );

                }


            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3><img src="images/mail.png">Gestion Mensajeria</h3>
            <hr>
            <form action="SrvDownloadDocMensajeria" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-3">
                            <label for="oficina">Oficina Comercial</label>
                            <select name="oficina" id="oficina" class="form-control input-sm">
                                <option value="all">Todas</option>
                                <% for (Oficina oficina : lista1) {%>
                                <option value="<%= oficina.getCodigo()%>"><%= oficina.getNombre()%></option>
                                <% } %>
                            </select>

                        </div>
                        <div class="col-xs-3">
                            <label for="delegacion">Delegacion</label>
                            <select name="delegacion" id="delegacion" class="form-control input-sm">
                                <option value="all">Todas</option>
                                <% for (Delegacion delegacion : lista3) {%>
                                <option value="<%= delegacion.getCodigo()%>"><%= delegacion.getNombre()%></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <label for="departamento">Departamento</label>
                            <select name="departamento" id="departamento" class="form-control input-sm">
                                <option value="all">Todos</option>
                                <% for (Departamento departamento : lista4) {%>
                                <option value="<%= departamento.getCodigo()%>"><%= departamento.getNombre()%></option>
                                <% } %>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-3">
                            <label for="empresa">Empresa mensajeria</label>
                            <select name="empresa" id="empresa" class="form-control input-sm">
                                <option value="">Seleccionar</option>
                                <% for (Empresa empresa : lista2) {%>
                                <option value="<%= empresa.getCodigo()%>"><%= empresa.getNombre()%></option>
                                <% }%>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <label for="fecha1">Fecha Inicial</label>
                            <div class="input-group date form_datetime" data-date="" data-date-format="dd/mm/yyyy H:i" data-link-field="fecha1" data-link-format="dd/mm/yyyy H:i" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha1" name="fecha1" value="<%= fecha%>"  />
                        </div>
                        <div class="col-xs-3">
                            <label for="fecha2">Fecha Final</label>
                            <div class="input-group date form_datetime" data-date="" data-date-format="dd/mm/yyyy H:i" data-link-field="fecha2" data-link-format="dd/mm/yyyy H:i" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha2" name="fecha2" value="<%= fecha%>"  />
                        </div>

                    </div>


                </div>
                <div class="form-group">
                    <button type="button" onclick="javascript:filtrar();" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Consultar</button>
                    <button type="submit" name="cmd_descargar" id="cmd_descargar" class="btn btn-primary btn-sm">Descargar</button>
                    <div id="info"></div>
                </div>

                <div class="alert alert-success alert-dismissible" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <strong>Aviso!</strong>
                    <p>
                        Una vez termine el proceso de carga de la plantilla, el usuario dispone de 15 minutos para revisar la plantilla.
                        Durante este tiempo no se podrá descargar la plantilla o marcar el archivo como impreso.

                    </p>

                </div>
                <div class="upload-msg"></div>
            </form>
            <div id="lista"></div>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="js/locales/bootstrap-datetimepicker.es.js" charset="UTF-8"></script>
    <script type="text/javascript">
                        $(".form_datetime").datetimepicker({
                            language: 'es',
                            format: 'dd/mm/yyyy hh:ii',
                            weekStart: 1,
                            todayBtn: 1,
                            autoclose: 1,
                            todayHighlight: 1,
                            startView: 2,
                            forceParse: 0,
                            showMeridian: 1
                        });
    </script>


</html>
<%
    conexion.Close();
%>
