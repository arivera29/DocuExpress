<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<%    db conexion = new db();
    ControladorOficina c1 = new ControladorOficina(conexion);
    ArrayList<Oficina> lista1 = c1.list();
    ControladorEmpresa c2 = new ControladorEmpresa(conexion);
    ArrayList<Empresa> lista2 = c2.List(1);

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
        <script>
            function list() {
                var url = "table_archivos.jsp";
                $("#lista").load(url, function () {
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
    <body onload="javascript:list()">
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3><img src="images/upload.png"> Subir Documentos</h3>
            <p>
                Descargar plantilla. <a href="plantillas/FORMATO_CARGA_DOCUMENTOS.xlsx" title="Descargar plantilla"><img src="images/download.png"></a>
            </p>
            <p>
                <img src="images/information.png"> El formato del archivo debe ser Texto separado por tabulador.
            </p>
            <hr>
            <form action="" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="oficina">Oficina Comercial</label>
                            <select name="oficina" id="oficina" class="form-control input-sm">
                                <option value="">Seleccionar</option>
                                <% for (Oficina oficina : lista1) {%>
                                <option value="<%= oficina.getCodigo()%>"><%= oficina.getNombre()%></option>
                                <% } %>
                            </select>

                        </div>
                        <div class="col-xs-3">
                            <label for="empresa">Empresa mensajeria</label>
                            <select name="empresa" id="empresa" class="form-control input-sm">
                                <option value="">Seleccionar</option>
                                <% for (Empresa empresa : lista2) {%>
                                <option value="<%= empresa.getCodigo()%>"><%= empresa.getNombre()%></option>
                                <% } %>
                            </select>

                        </div>
                        <div class="col-xs-3">
                            <label for="archivo">Archivo Plantilla</label>
                            <input type="file" name="archivo" id="archivo" class="form-control input-sm">
                            <p class="help-block">Seleccion un archivo.</p>
                        </div>
                    </div>


                </div>
                <div class="form-group">
                    <button type="button" onclick="javascript:upload_file();" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Subir</button>
                </div>
                <div class="alert alert-success alert-dismissible" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <strong>Aviso!</strong>
                    <p>
                        Una vez termine el proceso de carga de la plantilla, el usuario dispone de 15 minutos para revisar la plantilla.
                        Después de este tiempo no se podrá eliminar la plantilla, se deberá informar al administrador del sistema.
                        
                    </p>

                </div>            

                <div class="upload-msg"></div>
            </form>
            <div id="info"></div>
            <div id="lista"></div>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
    <script>
                        function upload_file() {//Funcion encargada de enviar el archivo via AJAX
                            var oficina = $("#oficina").val();
                            var empresa = $("#empresa").val();

                            if (oficina == "" || empresa == "") {
                                alert("Debe ingresar la Oficina Comercial y la Empresa de mensajeria");
                                return;
                            }


                            $(".upload-msg").text('Cargando...');
                            var inputFile = document.getElementById("archivo");
                            var file = inputFile.files[0];
                            var data = new FormData();
                            data.append('archivo', file);
                            data.append('oficina', oficina);
                            data.append('empresa', empresa);
                            $.ajax({
                                url: "upload_plantilla2.jsp", // Url to which the request is send
                                type: "POST", // Type of request to be send, called as method
                                data: data, // Data sent to server, a set of key/value pairs (i.e. form fields and values)
                                contentType: false, // The content type used when sending data to the server.
                                cache: false, // To unable request pages to be cached
                                processData: false, // To send DOMDocument or non processed data file it is set to false
                                success: function (data)   // A function to be called if request succeeds
                                {
                                    $(".upload-msg").html(data);
                                    list();

                                }
                            });

                        }
    </script>
</html>
<%
    conexion.Close();
%>
