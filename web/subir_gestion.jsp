<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<%
    db conexion = new db();
    ControladorOficina c1 = new ControladorOficina(conexion);
    ArrayList<Oficina> lista1 = c1.list();
    ControladorEmpresa c2 = new ControladorEmpresa(conexion);
    ArrayList<Empresa> lista2 = c2.List(1);
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Subir Archivo Gestion</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3><img src="images/upload.png">Subir archivo Gestion</h3>
            <p>
                Descargar plantilla. <a href="plantillas/FORMATO_CARGA_GESTION.xlsx" title="Descargar plantilla"><img src="images/download.png"></a>
            </p>
                        <p>
                <img src="images/information.png"> El formato del archivo debe ser Texto separado por tabulador.
            </p>

            <hr>
            <form action="" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="archivo">Archivo Gestion</label>
                            <input type="file" name="archivo" id="archivo" class="form-control input-sm">
                            <p class="help-block">Seleccion un archivo.</p>
                        </div>
                    </div>
                    
                    
                </div>
                <div class="form-group">
                    <button type="button" onclick="javascript:upload_file();" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Subir</button>
                </div>
                <div class="upload-msg"></div>
            </form>
            <div id="lista"></div>
        </div>
    
        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
    <script>
        function upload_file(){//Funcion encargada de enviar el archivo via AJAX
				$(".upload-msg").text('Cargando...');
				var inputFile = document.getElementById("archivo");
				var file = inputFile.files[0];
				var data = new FormData();
				data.append('archivo',file);
				$.ajax({
					url: "upload_gestion.jsp",        // Url to which the request is send
					type: "POST",             // Type of request to be send, called as method
					data: data,               // Data sent to server, a set of key/value pairs (i.e. form fields and values)
					contentType: false,       // The content type used when sending data to the server.
					cache: false,             // To unable request pages to be cached
					processData:false,        // To send DOMDocument or non processed data file it is set to false
					success: function(data)   // A function to be called if request succeeds
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
