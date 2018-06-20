<%-- 
    Document   : upload_plantilla
    Created on : 27-ago-2016, 16:07:29
    Author     : Aimer
--%>
<%@page import="com.are.docuexpress.controlador.ControladorDocumento"%>
<%@page import="com.are.docuexpress.entidad.Documento"%>
<%@page import="com.csvreader.CsvReader"%>
<%@page import="com.are.docuexpress.entidad.Archivo"%>
<%@page import="com.are.docuexpress.controlador.ControladorArchivo"%>
<%@page import="com.are.docuexpress.controlador.db"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.are.docuexpress.controlador.Utilidades"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.UUID" %>

<%
    boolean error = false;
    String msgError = "";
    String aviso = "";
    String usuario = (String) session.getAttribute("usuario");
    Archivo archivo = null;
    FileItem file = null;

    ServletContext servletContext = getServletContext();
    UUID idFilename = UUID.randomUUID();
    String nombreReal = idFilename + ".txt";
    String filename = servletContext.getRealPath("/upload") + File.separator + nombreReal;

    /*FileItemFactory es una interfaz para crear FileItem*/
    FileItemFactory file_factory = new DiskFileItemFactory();
    String cadena = "";
    /*ServletFileUpload esta clase convierte los input file a FileItem*/
    ServletFileUpload servlet_up = new ServletFileUpload(file_factory);
    /*sacando los FileItem del ServletFileUpload en una lista */
    try {
        List items = servlet_up.parseRequest(request);

        Iterator iter = items.iterator();

        while (iter.hasNext()) {
            /*FileItem representa un archivo en memoria que puede ser pasado al disco duro*/
            FileItem item = (FileItem) iter.next();
            /*item.isFormField() false=input file; true=text field*/
            if (!item.isFormField()) {
                /*cual sera la ruta al archivo en el servidor*/
                file = item;
            }
        }

        if (file != null) {
            db conexion = null;
            try {
                File archivo_server = new File(filename);
                file.write(archivo_server);
                if (archivo_server.exists()) {

                    aviso = "Archivo guardado correctamente... " + filename;
                    CsvReader reader = new CsvReader(filename);
                    reader.setDelimiter('	'); // tabulador
                    reader.readHeaders();

                    String[] headers = reader.getHeaders();
                    if (headers.length != 4) { // estan las columnas OK.
                        throw new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura");
                    }

                    conexion = new db();
                    int contador = 0;
                    int registros = 0;
                    while (reader.readRecord()) {
                        registros++;
                        String sql = "UPDATE guias SET estadoGuia=?, fechaGestion=?,usuarioGestion=?,causalDevolucion=?"
                                + " WHERE numeroGuia=?";
                        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                        pst.setString(1, reader.get(1));
                        pst.setString(2, reader.get(2));
                        pst.setString(3, usuario);
                        pst.setString(4, reader.get(3));
                        pst.setString(5, reader.get(0));
                        try {
                            if (conexion.Update(pst) > 0) {
                                contador++;
                            }
                        } catch (SQLException e) {
                            error = true;
                            msgError = "Error: " + e.getMessage();
                        }
                    }
                    if (contador > 0) {
                        conexion.Commit();
                        aviso = "Archivo procesado, numero de registros agregados " + contador + " de " + registros;
                    } else {
                        conexion.Rollback();
                        aviso = "Archivo NO procesado, numero de registros agregados " + contador + " de " + registros;
                    }

                } else {
                    error = true;
                    msgError = "Error al guardar el registro en la base de datos";
                }
            } catch (SQLException e) {
                error = true;
                msgError = "Error: " + e.getMessage();
            } catch (Exception e) {
                error = true;
                msgError = "Error: " + e.getMessage();
            } finally {
                if (conexion != null) {
                    conexion.Close();
                }
            }
        }
    } catch (FileUploadException ex) {
        error = true;
        msgError = "Error encountered while parsing the request" + ex.getMessage();
    } catch (SQLException ex) {
        error = true;
        msgError = "Error encountered while save record bd " + ex.getMessage();

    } catch (Exception ex) {
        error = true;
        msgError = "Error encountered while uploading file: " + ex.getMessage();
    }
%>
<% if (error) {%>
<div class="alert alert-danger alert-dismissible" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <strong>Error!</strong> 
    <%=  "<p>" + msgError + "</p>"%>
</div>
<% } %>
<% if (!aviso.equals("")) {%>
<div class="alert alert-success alert-dismissible" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <strong>Aviso!</strong> 
    <%=  "<p>" + aviso + "</p>"%>
</div>
<% }%>