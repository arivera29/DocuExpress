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
    ControladorTipoDocumento c2 = new ControladorTipoDocumento(conexion);
    ArrayList<TipoDocumento> lista2 = c2.list();
    ControladorDelegacion c3 = new ControladorDelegacion(conexion);
    ArrayList<Delegacion> lista3 = c3.list();
    ControladorDepartamento c4 = new ControladorDepartamento(conexion);
    ArrayList<Departamento> lista4 = c4.list();

    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fecha = formateador.format(new Date());

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reporte de Gestion</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3><img src="images/download.png">Reporte de Gestion</h3>
            <hr>
            <form action="SrvDownloadRpGestion" name="form1" id="form1" role="form">
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
                            <label for="tipo">Tipo Documento</label>
                            <select name="tipo" id="tipo" class="form-control input-sm">
                                <option value="all">Todos</option>
                                <% for (TipoDocumento tipo : lista2) {%>
                                <option value="<%= tipo.getCodigo()%>"><%= tipo.getNombre()%></option>
                                <% }%>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <label for="fecha1">Fecha Inicial</label>
                            <div class="input-group date form_date" data-date="" data-date-format="dd/mm/yyyy" data-link-field="fecha1" data-link-format="dd/mm/yyyy" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha1" name="fecha1" value="<%= fecha%>"  />
                        </div>
                        <div class="col-xs-3">
                            <label for="fecha2">Fecha Final</label>
                            <div class="input-group date form_date" data-date="" data-date-format="dd/mm/yyyy" data-link-field="fecha2" data-link-format="dd/mm/yyyy" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha2" name="fecha2" value="<%= fecha%>"  />
                        </div>

                    </div>


                </div>
                <div class="form-group">
                    <button type="submit" name="cmd_descargar" id="cmd_descargar" class="btn btn-primary btn-sm">Descargar</button>
                    <div id="info"></div>
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
                        $(".form_date").datetimepicker({
                            language: 'es',
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
