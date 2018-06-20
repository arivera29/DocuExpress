<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.docuexpress.controlador.*" %>
<%@ page import="com.are.docuexpress.entidad.*" %>
<%    if (request.getParameter("codigo") == null) {
        response.sendRedirect("TipoNoConformidad.jsp");
        return;
    }

    String codigo = (String) request.getParameter("codigo");
    db conexion = new db();
    ControladorTipoNoConformidad manejador = new ControladorTipoNoConformidad(conexion);
    if (!manejador.find(codigo)) {
        response.sendRedirect("TipoNoConformidad.jsp");
        return;
    }

    TipoNoConformidad tipo = manejador.getTipo();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Modificar Tipo de No Conformidad</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <script type="text/javascript">
            function modificar(key) {
                var codigo = $("#codigo").val();
                var nombre = $("#nombre").val();
                var estado = $("#estado").val();
                var correo1 = $("#correo1").val();
                var correo2 = $("#correo2").val();
                var nivel = $("#nivel").val();
                var responsable = $("#responsable").val();

                if (codigo == "" || nombre == "") {
                    $("#info").html("<img src=\"images/warning.jpg\">Faltan datos");
                    return;
                }
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvTipoNoConformidad",
                        {
                            operacion: "update",
                            codigo: codigo,
                            nombre: nombre,
                            estado: estado,
                            correo1 : correo1,
                            correo2 : correo2,
                            nivel : nivel,
                            responsable : responsable,
                            key: key
                        },
                procesar

                        );

            }
            function procesar(resultado) {
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"images/warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/ok.gif\">Registro modificado correctamente");
                }
            }

            function eliminar(key) {
                if (confirm("Desea eliminar el registro " + key)) {
                    var cmd = document.getElementById("cmd_eliminar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvTipoNoConformidad",
                            {
                                operacion: "remove",
                                key: key
                            },
                    procesarEliminar

                            );
                }

            }

            function procesarEliminar(resultado) {
                var cmd = document.getElementById("cmd_eliminar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"images/warning.jpg\">" + resultado);
                } else {
                    alert("Registro eliminado correctamente");
                    window.location.href = "TipoNoConformidad.jsp";
                }

            }

            function cancelar() {
                window.location.href = "TipoNoConformidad.jsp";
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3>Modificar Tipo No Conformidad</h3>
            <p>
                Esta vista permite modificar el registro de Tipo de No Conformidad.  Debe diligenciar la informacion y dar clic en el boton Actualizar.
            </p>
            <div id="info"></div>
            <form action="" name="form1">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2">
                            <label for="codigo">Codigo</label>
                            <input type="text" name="codigo" id="codigo" class="form-control input-sm" placeholder="Ingrese codigo" value="<%= tipo.getCodigo()%>">
                        </div>
                        <div class="col-xs-4">
                            <label for="nombre">Nombre</label>
                            <input type="text" name="nombre" id="nombre" class="form-control input-sm" placeholder="Ingrese descripcion" value="<%= tipo.getNombre()%>">
                        </div>
                        <div class="col-xs-2">
                            <label for="nivel">Nivel criticidad</label>
                            <select name="nivel" id="nivel" class="form-control input-sm">
                                <option value="1" <%= tipo.getNivelCriticidad() == 1 ? "selected" : ""%>>Bajo</option>
                                <option value="2" <%= tipo.getNivelCriticidad() == 2 ? "selected" : ""%>>Medio</option>
                                <option value="3" <%= tipo.getNivelCriticidad() == 3 ? "selected" : ""%>>Alto</option>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label for="responsable">Reponsable</label>
                            <select name="responsable" id="responsable" class="form-control input-sm">
                                <option value="1" <%= tipo.getResponsable() == 1 ? "selected" : ""%>>Central de escritos</option>
                                <option value="2" <%= tipo.getResponsable() == 2 ? "selected" : ""%>>Operador Mensajeria</option>
                            </select>
                        </div>



                    </div>
                    <div class="row">
                        <div class="col-xs-4">
                            <label for="correo1">Correo Notificacion 1</label>
                            <input type="text" name="correo1" id="correo1" value="<%= tipo.getCorreoNotificacion1() %>" class="form-control input-sm" placeholder="Ingrese correo Notificacion">
                        </div>
                        <div class="col-xs-4">
                            <label for="correo2">Correo Notificacion 2</label>
                            <input type="text" name="correo2" id="correo2" value="<%= tipo.getCorreoNotificacion2() %>" class="form-control input-sm" placeholder="Ingrese correo Notificacion">
                        </div>
                        <div class="col-xs-2">
                            <label for="estado">Activo</label>
                            <select name="estado" id="estado" class="form-control input-sm">
                                <option value="1" <%= tipo.getEstado() == 1 ? "selected" : ""%>  >Si</option>
                                <option value="0" <%= tipo.getEstado() == 0 ? "selected" : ""%>>No</option>
                            </select>
                        </div>
                    </div>

                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary" onclick="javascript:modificar('<%= (String) request.getParameter("codigo")%>');" id="cmd_modificar" name="cmd_modificar" >Actualizar</button> 
                    <button type="button" class="btn btn-primary" onclick="javascript:eliminar('<%= (String) request.getParameter("codigo")%>');" id="cmd_eliminar" name="cmd_eliminar" >Eliminar</button>  
                    <button type="button" class="btn btn-primary" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar()" >Cancelar</button>
                </div> 
            </form>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
</html>

<%
    conexion.Close();
%>