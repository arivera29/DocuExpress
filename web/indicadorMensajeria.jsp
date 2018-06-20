<%-- 
    Document   : indicadorMensajeria
    Created on : 05-sep-2017, 4:54:54
    Author     : Aimer
--%>

<%@page import="com.are.docuexpress.controlador.Utilidades"%>
<%@page import="com.are.docuexpress.entidad.IndicadorMensajeria"%>
<%@page import="com.are.docuexpress.controlador.ControladorIndicadorMensajeria"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%
    db conexion = new db();
    ControladorIndicadorMensajeria controlador = new ControladorIndicadorMensajeria(conexion);
    controlador.Calcular(new java.util.Date());
    IndicadorMensajeria indicador = controlador.getIndicador();
    controlador.Calcular(Utilidades.sumarRestarDiasFecha(new java.util.Date(), -1));
    IndicadorMensajeria indicador2 = controlador.getIndicador();
%>

<div id="panel_idicador">
    <h4>Indicadores Web Services Mensajeria</h4>
    <h5>Hoy</h5>
    <div class="row">
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Pendiente Guia</div>
                <div class="panel-body caja" >
                    <p class="text-center indicador"><%= indicador.getPeticionPCG()%></p>
                </div>
            </div>        
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Pendiente Gestión</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador.getPeticionPG()%></p>
                </div>
            </div> 
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Generar Guia</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador.getPeticionGuia()%></p>
                </div>
            </div> 
        </div>
    </div>
    <div class="row">
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Imagen</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador.getPeticionUploadImagen()%></p>
                </div>
            </div>        
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Entrega</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador.getPeticionEntrega()%></p>
                </div>
            </div> 
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Devolucion</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador.getPeticionDevolucion()%></p>
                </div>
            </div> 
        </div>
    </div>

    <h5>Ayer</h5>
    <div class="row">
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Pendiente Guia</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador2.getPeticionPCG()%></p>
                </div>
            </div>        
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Pendiente Gestion</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador2.getPeticionPG()%></p>
                </div>
            </div> 
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Generar Guia</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador2.getPeticionGuia()%></p>
                </div>
            </div> 
        </div>
    </div>
    <div class="row">
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Imagen</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador2.getPeticionUploadImagen()%></p>
                </div>
            </div>        
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Entrega</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador2.getPeticionEntrega()%></p>
                </div>
            </div> 
        </div>
        <div class="col-xs-4">
            <div class="panel panel-default">
                <div class="panel-heading">Peticiones Devolucion</div>
                <div class="panel-body caja">
                    <p class="text-center indicador"><%= indicador2.getPeticionDevolucion()%></p>
                </div>
            </div> 
        </div>
    </div>
</div>

<%
    conexion.Close();
%>