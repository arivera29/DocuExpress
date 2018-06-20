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
    

    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fecha = formateador.format(new Date());

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reporte pendiente Mensjeria</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
        <script src="js/bootstrap-table.js"></script>
        
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3>Reporte Pendiente Mensajeria</h3>
            <p>
                Este reporte genera un archivo csv (separado por ;) con los documentos que no tienen guia generada y/o no se ha subido aún la imagen de la guía.
            </p>
            <hr>
            <form action="SrvDownloadPendienteMensajeria" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="oficina">Oficina Comercial</label>
                            <select name="oficina" id="oficina" class="form-control input-sm">
                                <option value="all">Todas</option>
                                <% for (Oficina oficina : lista1) {%>
                                <option value="<%= oficina.getCodigo()%>"><%= oficina.getNombre()%></option>
                                <% } %>
                            </select>

                        </div>

<!--                        <div class="col-xs-2">
                            <label for="fecha1">Fecha Inicial</label>
                            <div class="input-group date form_date" data-date="" data-date-format="dd/mm/yyyy" data-link-field="fecha1" data-link-format="dd/mm/yyyy" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                                <input type="hidden" id="fecha1" name="fecha1" value="<%= fecha%>"  />
                        </div>
                        <div class="col-xs-2">
                            <label for="fecha2">Fecha Final</label>
                            <div class="input-group date form_date" data-date="" data-date-format="dd/mm/yyyy" data-link-field="fecha2" data-link-format="dd/mm/yyyy" >
                                <input class="form-control input-sm" size="24" type="text" value="<%= fecha%>" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <input type="hidden" id="fecha2" name="fecha2" value="<%= fecha%>"  />
                        </div>-->
                    </div>


                </div>
                <div class="form-group">
                    <button type="submit" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Descargar</button>
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
