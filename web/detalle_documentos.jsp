<%-- 
    Document   : subir_pdf
    Created on : 27-ago-2016, 15:43:44
    Author     : Aimer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="validausuario.jsp"%>
<%
    db conexion = new db();
    ControladorArchivo controlador = new ControladorArchivo(conexion);
    String id = (String)request.getParameter("id");
    if (!controlador.find(Integer.parseInt(id))) {
        response.sendRedirect("subir_pdf.jsp");
        return;
    }
    
    Archivo archivo = controlador.getArchivo();
    String usuario = (String)session.getAttribute("usuario");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ver Documentos</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <script>
            function list(id) {
                var url = "listado_plantilla.jsp";
                $("#lista1").load(url,{
                   id : id 
                }, function () {
                    $(".table").bootstrapTable({
	            pagination: true,
	            paginationLoop: false,
	            search :true
	        });
                });
                
                var url = "table_consulta_impresion.jsp";
                $("#lista2").load(url,{
                   id : id 
                }, function () {
                    $(".table").bootstrapTable({
	            pagination: true,
	            paginationLoop: false,
	            search :true
	        });
                });
            }
            
            function eliminar(id) {
                if (confirm ("Esta seguro de eliminar el archivo?")) {
                    var url = "SrvDeleteFile";
                    $.post(url,{
                        id : id
                    }, function (data) {
                        
                        if (data == "OK") {
                            alert("Arvhivo eliminado correctamente");
                            var url = "table_consulta_impresion.jsp";
                            $("#lista2").load(url,{
                               id : <%= archivo.getId() %>
                            }, function () {
                                $(".table").bootstrapTable({
                                pagination: true,
                                paginationLoop: false,
                                search :true
                            });
                            });
                        }else {
                            alert(data);
                        }
                        
                        
                    });
                    
                }
                
            }
            
        </script>
    </head>
    <body onload="javascript:list(<%= archivo.getId() %>)">
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3>Documentos Plantilla</h3>
            <hr>
            <a href="SrvDownloadPlantilla?id=<%= archivo.getId() %>" class="btn btn-primary btn-sm">Descargar Plantilla</a>
            <a href="SrvDownloadPlantilla2?id=<%= archivo.getId() %>" class="btn btn-primary btn-sm">Descargar Plantilla 2</a>
            <div id="lista1"></div>
            <h3>Documentos de Impresion</h3>
            <% if (usuario.equals("admin") || perfil.equals("1")) {  %>
            <a href="subir_pdf.jsp?id=<%= archivo.getId() %>" class="btn btn-primary btn-sm">Subir Archivo impresion</a>
            <% }  %>
            <div id="lista2"></div>
            
        </div>
    
        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
</html>
<%
    conexion.Close();
%>
