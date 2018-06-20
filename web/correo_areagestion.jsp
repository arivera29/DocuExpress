<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%    if (request.getParameter("codigo") == null) {
        response.sendRedirect("AreaGestion.jsp");
        return;
    }

    String codigo = (String) request.getParameter("codigo");
    db conexion = new db();
    ControladorAreaGestion manejador = new ControladorAreaGestion(conexion);
    if (!manejador.find(Integer.parseInt(codigo))) {
        response.sendRedirect("AreaGestion.jsp");
        return;
    }

    AreaGestion area = manejador.getArea();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Modificar Delegacion</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <script type="text/javascript">
            function agregar(key) {

                var correo = $("#correo").val();
                if (correo == "") {
                    $("#info").html("<img src=\"images/warning.jpg\">Faltan datos");
                    return;
                }
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvAreaGestion",
                        {
                            operacion: "addCorreo",
                            correo: correo,
                            key: key
                        },
                procesar

                        );

            }
            function procesar(resultado) {
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = false;
                if (resultado.trim() != 'OK') {
                    $("#info").html("<img src=\"images/warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/ok.gif\">Registro editado correctamente");
                }
            }

            function eliminar(area,key) {
                if (confirm("Desea eliminar el registro ")) {
                    var cmd = document.getElementById("cmd_eliminar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvAreaGestion",
                            {
                                operacion: "removeCorreo",
                                key: key
                            },
                    function (resultado) {
                        var cmd = document.getElementById("cmd_eliminar");
                        cmd.disabled = false;
                        if (resultado != 'OK') {
                            $("#info").html("<img src=\"images/warning.jpg\">" + resultado);
                        } else {
                            list(area);
                            alert("Registro eliminado correctamente");
                        }
                    }

                    );
                }

            }

            function cancelar() {
                window.location.href = "AreaGestion.jsp";
            }

            function list(key) {
                $("#list").html("<img src=\"images/loading.gif\" > Consultando...");
                $("#list").load("SrvAreaGestion?operacion=listCorreo",{
                    key: key
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
            <h3>Modificar Area de Gestion</h3>
            <p>
                Esta vista permite agregar o eliminar correos de notificacion del area de gestión.  Debe diligenciar la informacion y dar clic en el boton Guardar.
            </p>
            <div id="info"></div>
            <form action="" name="form1">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2">
                            <label for="codigo">ID</label>
                            <input type="text" name="codigo" id="codigo" class="form-control input-sm" placeholder="Ingrese codigo" value="<%= area.getId()%>" readonly>
                        </div>
                        <div class="col-xs-4">
                            <label for="nombre">Nombre</label>
                            <input type="text" name="nombre" id="nombre" class="form-control input-sm" placeholder="Ingrese descripcion" value="<%= area.getNombre()%>" readonly>
                        </div>
                    </div>

                </div>
                <hr>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="nombre">Correo Notificación</label>
                            <input type="mail" name="correo" id="correo" class="form-control input-sm" placeholder="Ingrese correo" value="">
                        </div>

                    </div>
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary" onclick="javascript:agregar('<%= (String) request.getParameter("codigo")%>');" id="cmd_modificar" name="cmd_modificar" >Guardar</button> 
                    <button type="button" class="btn btn-primary" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar()" >Cancelar</button>
                </div> 
            </form>

            <div id="list"></div>
        </div>

        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
</html>

<%
    conexion.Close();
%>