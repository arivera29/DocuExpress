<%@ page import="java.util.*"%>
<%@ page import="com.are.docuexpress.controlador.*"%>
<%@ page import="com.are.docuexpress.entidad.*"%>
<%
    ControladorSuscripcion cs = new ControladorSuscripcion();
    if (!cs.verify()) {
        response.sendRedirect("suspend.jsp");
        return;
    }

    String perfil = (String) session.getAttribute("perfil");
    String user = (String) session.getAttribute("usuario");

%>

<nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="main.jsp">ELECTRICARIBE</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <% if (perfil.equals("1")) { // Administrador %>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Configuracion <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="delegacion.jsp">Delegacion</a></li>
                        <li><a href="departamento.jsp">Departamentos</a></li>
                        <li><a href="oficina.jsp">Oficinas Comerciales</a></li>
                        <li><a href="Empresa.jsp">Empresa Mensajeria</a></li>
                        <li class="divider"></li>
                        <li><a href="TipoDocumento.jsp">Tipo Documentos</a></li>
                        <li><a href="TipoArchivo.jsp">Tipo Archivos</a></li>
                        <li><a href="TipoNoConformidad.jsp">Tipo No Conformidad</a></li>
                        <li class="divider"></li>
                        <li><a href="smartphone.jsp">Smartphone</a></li>
                        <li><a href="causal.jsp">Causal Devolucion</a></li>

                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Mensajeria <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="subir_plantilla.jsp">Subir Documentos</a></li>
                        <li><a href="consulta_documentos.jsp">Consulta Documentos</a></li>
                        <li><a href="impresion_pdf.jsp">Gestion</a></li>
                        <li class="divider"></li>
                        <li><a href="subir_guias.jsp">Archivo Guias</a></li>
                        <li><a href="subir_gestion.jsp">Archivo Gestion</a></li>
                        <li class="divider"></li>
                        <li><a href="confirmar_guias.jsp">Confirmar Guia</a></li>
                        <li><a href="eliminar_guias.jsp">Eliminar Guias</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Consultas <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="consulta_doc_oficinas.jsp">Documentos por Oficina</a></li>
                        <li><a href="consulta_docs_fecha.jsp">Documentos x Fecha</a></li>
                        <li><a href="consulta_estado_mensajeria.jsp">Estado Mensajeria</a></li>
                        <li class="divider"></li>
                        <li><a href="consulta_documento.jsp">Buscar Documento</a></li>
                        <li><a href="consulta_guia.jsp">Buscar Guia</a></li>
                        <li><a href="consulta_guias_fecha.jsp">Consulta Guias x Fecha</a></li>
                        <li><a href="consulta_guias_imagen.jsp">Descarga archivo Guias</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Reportes <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="reporte_pendiente_mensajeria.jsp">Pendiente Mensajeria</a></li>
                        <li><a href="reporte_gestion.jsp">Reporte Gestión</a></li>
                        <li><a href="reporte_correspondencia.jsp">Reporte Correspondencia</a></li>
                        <li><a href="consulta_correspondencia.jsp">Consulta Correspondencia</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Admin <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="usuario.jsp">Usuarios</a></li>
                    </ul>
                </li>
            </ul>

            <% } %>

            <% if (perfil.equals("2")) { // Coordinador %>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Mensajeria <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="subir_plantilla.jsp">Subir Documentos</a></li>
                        <li><a href="consulta_documentos.jsp">Consulta Documentos</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Consultas <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="consulta_doc_oficinas.jsp">Documentos por Oficina</a></li>
                        <li><a href="consulta_docs_fecha.jsp">Documentos x Fecha</a></li>
                        <li><a href="consulta_estado_mensajeria.jsp">Estado Mensajeria</a></li>
                        <li class="divider"></li>
                        <li><a href="consulta_documento.jsp">Buscar Documento</a></li>
                        <li><a href="consulta_guia.jsp">Buscar Guia</a></li>
                        <li><a href="consulta_guias_fecha.jsp">Consulta Guias x Fecha</a></li>
                        <li><a href="consulta_guias_imagen.jsp">Descarga archivo Guias</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Reportes <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="reporte_pendiente_mensajeria.jsp">Pendiente Mensajeria</a></li>
                        <li><a href="reporte_gestion.jsp">Reporte Gestión</a></li>
                        <li><a href="reporte_correspondencia.jsp">Reporte Correspondencia</a></li>
                        <li><a href="consulta_correspondencia.jsp">Consulta Correspondencia</a></li>
                    </ul>
                </li>
            </ul>

            <% } %>

            <% if (perfil.equals("3")) { // Impresión %>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Mensajeria <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="subir_plantilla.jsp">Subir Documentos</a></li>
                        <li><a href="consulta_documentos.jsp">Consulta Documentos</a></li>
                    </ul>
                </li>
            </ul>

            <% } %>

            <% if (perfil.equals("4")) { // Mensajeria %>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Mensajeria <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="impresion_pdf.jsp">Gestion</a></li>
                        <li class="divider"></li>
                        <li><a href="subir_guias.jsp">Archivo Guias</a></li>
                        <li><a href="subir_gestion.jsp">Archivo Gestion</a></li>
                        <li class="divider"></li>
                        <li><a href="confirmar_guias.jsp">Confirmar Guia</a></li>
                    </ul>
                </li>
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Consultas <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="consulta_estado_mensajeria.jsp">Estado Mensajeria</a></li>
                            <li><a href="consulta_guia.jsp">Buscar Guia</a></li>
                            <li><a href="consulta_guias_fecha.jsp">Consulta Guias x Fecha</a></li>
                            <li><a href="consulta_guias_imagen.jsp">Descarga archivo Guias</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Reportes <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="reporte_gestion.jsp">Reporte Gestión</a></li>
                        </ul>
                    </li>
                </ul>
            </ul>
            </ul>



            <% } %>
            <% if (perfil.equals("5")) {  // Oficina Comercial %>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Mensajeria <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="subir_plantilla.jsp">Subir Documentos</a></li>
                        <li><a href="consulta_documentos.jsp">Consulta Documentos</a></li>
                        <li><a href="impresion_pdf.jsp">Gestion</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Consultas <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="consulta_doc_oficinas.jsp">Documentos por Oficina</a></li>
                        <li><a href="consulta_docs_fecha.jsp">Documentos x Fecha</a></li>
                        <li><a href="consulta_estado_mensajeria.jsp">Estado Mensajeria</a></li>
                        <li class="divider"></li>
                        <li><a href="consulta_documento.jsp">Buscar Documento</a></li>
                        <li><a href="consulta_guia.jsp">Buscar Guia</a></li>
                        <li><a href="consulta_guias_fecha.jsp">Consulta Guias x Fecha</a></li>
                        <li><a href="consulta_guias_imagen.jsp">Descarga archivo Guias</a></li>
                    </ul>
                </li>
            </ul>

            <% }%>


            <ul class="nav navbar-nav navbar-right">
                <li><a href="logout.jsp">SALIR</a></li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>