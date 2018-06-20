<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%    String id = (String) request.getParameter("id");
    String tipo = (String) request.getParameter("tipo");
    db conexion = new db();

    String sql = "SELECT archivo_impresion.*, nomTipoArchivo "
            + " FROM archivo_impresion "
            + " INNER JOIN tipoArchivo ON codTipoArchivo = tipo"
            + " WHERE archivo_impresion.id = ? and estado=1";

    if (tipo.equals("2")) {
    sql = "SELECT d.*, a.fecha, o.nomOficina, a.confirmar "
            + " FROM documentos d "
            + " INNER JOIN archivos a ON a.id = d.idCarga "
            + " INNER JOIN oficina o ON a.oficina = o.codOficina "
            + " WHERE d.id = ? ";
    }

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    java.sql.ResultSet rs = conexion.Query(pst);
    rs.next();

    ControladorTipoNoConformidad controlador = new ControladorTipoNoConformidad(conexion);
    ArrayList<TipoNoConformidad> lista = controlador.list(1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>No conformidad</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <link rel="stylesheet" href="js/bootstrap-table.css">
        <script src="js/bootstrap-table.js"></script>
        <script type="text/javascript">
            function agregar(id, _tipo) {
                var observacion = $("#observacion").val();
                var tipo = $("#tipo").val();

                if (observacion == "" || tipo == "") {
                    $("#info").html("<img src=\"warning.jpg\">Falta informacion");
                    return;
                }

                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvNoConformidad",
                        {
                            operacion: "add",
                            id: id,
                            tipo: tipo,
                            observacion: observacion,
                            _tipo: _tipo
                        },
                function (resultado) {
                    var cmd = document.getElementById("cmd_agregar");
                    cmd.disabled = false;
                    if (resultado != 'OK') {
                        $("#info").html("<img src=\"images/warning.jpg\">" + resultado);
                    } else {
                        $("#info").html("<img src=\"images/ok.gif\">No conformidad agregada correctamente");
                        $("#observacion").val("");
                        list(id);
                    }
                }

                );

            }



            function list(id) {
                $("#list").html("<img src=\"images/loading.gif\" > Consultando...");
                $("#list").load("table_no_conformidad.jsp",
                        {
                            id: id
                        }, function () {
                    $(".table").bootstrapTable({
                        pagination: true,
                        paginationLoop: false,
                        search: true
                    });
                });
            }

            function cancelar(id) {
                document.location.href = "detalle_documentos_impresion.jsp?id=" + id;
            }

        </script>
    </head>
    <body onload="list(<%= (String) request.getParameter("id")%>)">
        <%@ include file="header.jsp" %>
        <div class="container">
            <h3>Registro no conformidad</h3>
            <p>
                Esta vista permite registrar una nueva no conformidad.  Debe diligenciar la informacion y dar clic en el boton Registrar.
            </p>
            <hr>
            
            <form action="" name="form1">
                <%  if (tipo.equals("1")) {%>
                <h4>Información archivo de impresión</h4>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2">
                            <label for="identificador">Identificador</label>
                            <input type="text" name="identificador" id="identificador" class="form-control input-sm" value="<%= rs.getString("id")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="fecha">Fecha Carga</label>
                            <input type="text" name="fecha" id="fecha" class="form-control input-sm" value="<%= rs.getString("fecha")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="tipo_archivo">Tipo Archivo</label>
                            <input type="text" name="tipo_archivo" id="tipo_archivo" class="form-control input-sm" value="<%= rs.getString("nomTipoArchivo")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="nombre_archivo">Nombre Archivo</label>
                            <input type="text" name="nombre_archivo" id="nombre_archivo" class="form-control input-sm" value="<%= rs.getString("local_filename")%>" readonly>
                        </div>
                    </div>
                </div>
                <% }  %>
                <%  if (tipo.equals("2")) {%>
                <h4>Información Documento</h4>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2">
                            <label for="identificador">Identificador</label>
                            <input type="text" name="identificador" id="identificador" class="form-control input-sm" value="<%= rs.getString("id")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="fecha">Fecha Carga</label>
                            <input type="text" name="fecha" id="fecha" class="form-control input-sm" value="<%= rs.getString("fechaCarga")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="nic">NIC</label>
                            <input type="text" name="nic" id="nic" class="form-control input-sm" value="<%= rs.getString("nic")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="tipo_documento">Tipo Documento</label>
                            <input type="text" name="tipo_documento" id="tipo_documento" class="form-control input-sm" value="<%= rs.getString("nomDocumento")%>" readonly>
                        </div>
                    </div>
                        <div class="row">
                        <div class="col-xs-2">
                            <label for="proceso">Nro. Proceso</label>
                            <input type="text" name="proceso" id="proceso" class="form-control input-sm" value="<%= rs.getString("numProceso")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="reclamante">Reclamante</label>
                            <input type="text" name="reclamante" id="reclamante" class="form-control input-sm" value="<%= rs.getString("nomReclamante")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="usuarioCarga">Uusuario Carga</label>
                            <input type="text" name="usuarioCarga" id="usuarioCarga" class="form-control input-sm" value="<%= rs.getString("usuarioCarga")%>" readonly>
                        </div>
                        <div class="col-xs-3">
                            <label for="direccion">Direccion</label>
                            <input type="text" name="direccion" id="direccion" class="form-control input-sm" value="<%= (String) rs.getString("via")%> <%= (String) rs.getString("crucero")%> <%= (String) rs.getString("placa")%>" readonly>
                        </div>
                    </div>
                </div>
                <% }  %>
                <hr>
                <h4>Información no conformidad</h4>
                <div class="form-group">
                    <label for="tipo">Tipo No Conformidad</label>
                    <select name="tipo" id="tipo" class="form-control input-sm">
                        <option value="">Seleccionar</option>
                        <% for (TipoNoConformidad item : lista) {%>
                        <option value="<%= item.getCodigo()%>"><%= item.getNombre()%></option>
                        <% }%>
                    </select>
                </div>
                <div class="form-group">
                    <label for="observacion">Observacion</label>
                    <textarea name="observacion" id="observacion" class="form-control input-sm"></textarea>
                </div>

                <div class="form-group">
                    <button type="button" class="btn btn-primary" name="cmd_agregar" id="cmd_agregar" onclick="javascript:agregar(<%= (String) request.getParameter("id")%>, '<%= (String) request.getParameter("tipo")%>')" >Registrar</button>
                    <button type="button" class="btn btn-primary" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar(<%= (String) request.getParameter("id")%>)" >Cancelar</button>
                </div>
                <div id="info"></div>
            </form>

            <div id="list"></div>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
    <script src="js/bootstrap.min.js"></script>
</html>
<%
    if (conexion != null) {
        conexion.Close();
    }
%>