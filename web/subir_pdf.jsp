<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<%
    db conexion = new db();
    ControladorTipoArchivo controlador = new ControladorTipoArchivo(conexion);
    ArrayList<TipoArchivo> lista = controlador.list();
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Subir Archivo de Impresion</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <script>
            function list(id) {
                var url = "table_archivos_impresion.jsp?id=" + id;
                $("#lista").load(url,function () {
                    $(".table").bootstrapTable({
	            pagination: true,
	            paginationLoop: false,
	            search :true
                });
            });
            }
            
            function eliminar(id, id_archivo) {
                if (confirm ("Esta seguro de eliminar el archivo?")) {
                    var url = "SrvDeleteFile";
                    $.post(url,{
                        id : id
                    }, function (data) {
                        
                        if (data == "OK") {
                            list(id_archivo);
                            alert("Archivo eliminado correctamente");
                            
                        }else {
                            alert(data);
                        }
                        
                        
                    });
                    
                }
                
            }
            function getNombreArchivo(fic) {
                fic = fic.split('\\');
                return fic[fic.length-1];
            }
            
            function validExtension(filename) {
                var validExtensions = ['pdf']; //array of valid extensions
                var Ext = filename.substr(filename.lastIndexOf('.') + 1);
                 if ($.inArray(Ext, validExtensions) == -1){
                     return false;
                 }
                
                return true;
            }
        </script>
    </head>
    <body onload="javascript:list('<%= (String)request.getParameter("id")  %>')">
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3><img src="images/upload.png"> Subir Archivo de Impresion</h3>
            <form action="" name="form1" id="form1" role="form">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-3">
                            <label for="tipo"> Tipo de archivo</label>
                            <select id="tipo" name="tipo" class="form-control">
                             <%  for (TipoArchivo tipo : lista)  {  %>
                             <option value = "<%= tipo.getCodigo() %>"><%= tipo.getNombre()  %></option>
                             <% } %>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <label for="archivo">Archivo Impresion</label>
                            <input type="file" name="archivo" id="archivo" class="form-control input-sm">
                            <p class="help-block">Seleccion un archivo.</p>
                        </div>
                    </div>
                    
                    
                </div>
                <div class="form-group">
                    <button type="button" onclick="javascript:upload_file(<%= (String)request.getParameter("id")  %>);" name="cmd_subir" id="cmd_subir" class="btn btn-primary btn-sm">Subir</button>
                </div>
                <div id="upload-msg"></div>
            </form>
            
            <div id="lista"></div>
        </div>
    
        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
    <script>
        function upload_file(id){//Funcion encargada de enviar el archivo via AJAX
            var filename = $("#archivo").val();
            var tipo = $("#tipo").val();
            
            if (!validExtension(filename)) {
                alert("Tipo de archivo no autorizado.  Verifique por favor");
                return ;
            }
        
            if (tipo == "01") {
                // Archivo es un Anexo
                filename = getNombreArchivo(filename);
                cadena = filename.split('_');
                if (cadena.length != 3) {
                    alert("Nombre del archivo no cumple el estandar 999999_9999999_XXXXXXXXXXXXX.pdf");
                    return;
                }
                var nic= cadena[0];
                var proceso = cadena[1];
                
                if (proceso.length != 7 ) {
                    alert("El No. PROCESO deben ser de 7 caracteres");
                    return;
                }
                /*if (!$.isNumeric(nic)) {
                    alert("El NIC " + nic + " Debe ser numero");
                    return;
                }*/
            
                if (!$.isNumeric(proceso)) {
                    alert("El Proceso " + proceso + " Debe ser numero");
                    return;
                }
            }
            
            $("#upload-msg").html("<img src='images/loading.gif'> Cargando...");
				var inputFile = document.getElementById("archivo");
				var file = inputFile.files[0];
				var data = new FormData();
                                
				data.append('archivo',file);
                                data.append('id',id);
                                data.append('tipo', tipo);
				$.ajax({
					url: "upload_pdf.jsp",        // Url to which the request is send
					type: "POST",             // Type of request to be send, called as method
					data: data,               // Data sent to server, a set of key/value pairs (i.e. form fields and values)
					contentType: false,       // The content type used when sending data to the server.
					cache: false,             // To unable request pages to be cached
					processData:false,        // To send DOMDocument or non processed data file it is set to false
					success: function(data)   // A function to be called if request succeeds
					{
						$("#upload-msg").html(data);
						list(id);
					}
				});
				
			}
    </script>
</html>
<%
    conexion.Close();
%>
