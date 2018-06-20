<%-- 
    Document   : upload_plantilla
    Created on : 27-ago-2016, 16:07:29
    Author     : Aimer
--%>
<%@page import="com.are.docuexpress.controlador.ControladorTipoDocumento"%>
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
    String oficina = "";
    String empresa = "";
    int idCarga = -1;
    Archivo archivo = null;
    FileItem file = null;
    int fila = 0;

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
            } else {
                if (item.getFieldName().equals("oficina")) {
                    oficina = item.getString();
                }
                if (item.getFieldName().equals("empresa")) {
                    empresa = item.getString();
                }
            }
        }
        if (file != null) {
            db conexion = null;
            try {
                File archivo_server = new File(filename);
                file.write(archivo_server);
                if (archivo_server.exists()) {

                    //aviso = "Archivo guardado correctamente... " + filename;
                    CsvReader reader = new CsvReader(filename);
                    reader.setDelimiter('	'); // tabulador
                    reader.readHeaders();

                    String[] headers = reader.getHeaders();
                    if (headers.length != 33) { // estan las columnas OK.
                        throw new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura (col=32). Columnas archivo " + headers.length);
                    }

                    conexion = new db();
                    conexion.BeginTransacction();

                    archivo = new Archivo();
                    archivo.setFilename(nombreReal);
                    archivo.setSize(file.getSize());
                    archivo.setOficina(oficina);
                    archivo.setEmpresa(empresa);
                    archivo.setUsuario(usuario);

                    ControladorArchivo controlador = new ControladorArchivo(conexion);

                    if (controlador.add(archivo)) {
                        idCarga = controlador.getLastIdCreated();
                        error = false;
                    } else {
                        error = true;
                        msgError = "Error al guardar el registro en la base de datos";
                        conexion.RollbackTransacction();
                    }

                    if (!error && idCarga != -1) {
                        int contador = 0;
                        int registros = 0;
                        ControladorTipoDocumento controladorTD = new ControladorTipoDocumento(conexion);
                        while (reader.readRecord()) {
                            registros++;
                            fila++;
                            try {
                                String fecha = reader.get(0);
                                
                                if (fecha.length() != 8) {
                                  throw new Exception("Formato fecha de entrega no válido");  
                                }
                                String fechaEntrega = fecha.substring(0,3) + "-" + fecha.substring(4, 5) + "-" + fecha.substring(6, 7);

                                String tipoDocumento = reader.get(5);
                                
                                if (tipoDocumento.equals("")) {
                                    throw new Exception("Tipo de documento está vacío");
                                }

                                if (!controladorTD.findByName(tipoDocumento)) {
                                    throw new Exception("Tipo de documento no registrado en la HGC");
                                }
                                
                                tipoDocumento = controladorTD.getTipo().getCodigo();

                                Documento documento = new Documento();
                                documento.setFechaEntrega(fechaEntrega);
                                documento.setNic(reader.get(2));
                                documento.setNumero(reader.get(3));
                                documento.setNombre(tipoDocumento);
                                documento.setMunicipio(reader.get(7));
                                documento.setBarrio(reader.get(8));
                                documento.setVia(reader.get(9));
                                documento.setCrucero(reader.get(10));
                                documento.setPlaca(reader.get(11));
                                documento.setReclamante(reader.get(12));
                                documento.setTelefono(reader.get(13));
                                documento.setProceso(reader.get(4)); // Identificador del proceso
                                documento.setCodigo(reader.get(1));
                                documento.setIdCarga(idCarga);
                                documento.setUsuarioCarga(usuario);
                                documento.setEstado(1);
                                documento.setRowFile(registros);
                                

                                ControladorDocumento controlador1 = new ControladorDocumento(conexion);
                                if (controlador1.Add(documento, false)) {
                                    contador++;
                                }
                            } catch (SQLException e) {
                                error = true;
                                msgError += "Error: " + e.getMessage() + " Fila: " + fila + "<br>";
                            } catch (Exception e) {
                                error = true;
                                msgError += "Error: " + e.getMessage() + " Fila: " + fila + "<br>";
                            }
                        }  // FIN LECTURA DE FILAS PLANTILLA

                        if (contador > 0 && contador == registros) {
                            conexion.CommitTransacction();
                            aviso = "Archivo procesado, numero de registros agregados " + contador + " de " + registros;
                        } else {
                            conexion.RollbackTransacction();
                            aviso = "Archivo NO procesado, Registros correctos  " + contador + " de " + registros;
                        }
                    } else {
                        error = true;
                        msgError = "Error consultando Id carga";
                    }
                } else {
                    error = true;
                    msgError = "Error al guardar el registro en la base de datos ";
                }
            } catch (SQLException e) {
                error = true;
                msgError = "Error: " + e.getMessage() + " Fila: " + fila + "<br>";
            } catch (Exception e) {
                error = true;
                msgError = "Error: " + e.getMessage() + " Fila: " + fila + "<br>";
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