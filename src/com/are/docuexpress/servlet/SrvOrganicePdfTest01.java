/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.db;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Aimer
 */
@WebServlet("/SrvOrganicePdfTest1")
public class SrvOrganicePdfTest01 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String path;
    private String path_log;
    private String file_log;
    private int numPag = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "impresion.pdf");
        response.setHeader(headerKey, headerValue);

        ServletContext servletContext = getServletContext();
        //PrintWriter out = response.getWriter();
        OutputStream outStream = response.getOutputStream();
        db conexion = null;

        List<Archivo> lista = new ArrayList<Archivo>();
        path = servletContext.getRealPath("/upload") + File.separator;
        path_log = servletContext.getRealPath("/log") + File.separator;
        java.util.Date hoy = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        file_log = path_log +  "organice_" + sdf.format(hoy) + ".txt";
        Document document = null;
        try {
            /* TODO output your page here. You may use following sample code. */

            conexion = new db();

            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outStream);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfReader reader;

            String id = (String) request.getParameter("id");  // Id del archivo

            String sql = "SELECT numProceso "
                    + " FROM documentos "
                    + " WHERE idCarga = ? "
                    + " ORDER BY rowFile";

            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, id);
            java.sql.ResultSet rsDoc = conexion.Query(pst);  // Contiene los #procesos

            sql = "SELECT filename, tipo, local_filename "
                    + " FROM archivo_impresion "
                    + " INNER JOIN TipoArchivo ON archivo_impresion.tipo = TipoArchivo.codTipoArchivo "
                    + " WHERE id_archivo = ? "
                    + " AND estado = 1"
                    + " ORDER BY TipoArchivo.ordTipoArchivo ";

            java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
            pst1.setString(1, id);
            java.sql.ResultSet rsArchivos = conexion.Query(pst1);  //Contiene los arhivos PDF
            while (rsArchivos.next()) {
                Archivo arc = new Archivo();
                arc.setPath(path + rsArchivos.getString("filename"));
                arc.setTipo(rsArchivos.getString("tipo"));
                arc.setLocalFilename(rsArchivos.getString("local_filename"));
                lista.add(arc);
            }

            // Recorrer documentos
            while (rsDoc.next()) {
                String word = rsDoc.getString("numProceso");
                this.Debub("Buscando proceso " + word);
                //out.println("Proceso: " + word + "<br/>");
                this.numPag = 0;
                for (Archivo archivo : lista) {  // buscar la palabra en todos el archivo
                    if (!archivo.getTipo().equals("01")) {  // Si no es anexo
                        this.Debub("Buscando en archivo " + archivo.getPath());
                        BuscarPaginaPDF(archivo.getPath(), word, document, writer, cb, archivo.getTipo());
                    } else {
                        this.Debub("Es un archivo de anexos" + archivo.getPath() );
                        String anexo = archivo.getLocalFilename();
                        String[] str = anexo.split("_");
                        if (str.length >= 2) {
                            String fname = str[1];
                            if (fname.trim().equals(word)) {
                                this.Debub("Agregando archivo de anexos: " + archivo.getPath());
                                this.AgregarAnexoPDF(word, archivo.getPath(), document, writer, cb);
                            }
                        }
                    }

                }
            }

        } catch (SQLException | DocumentException ex) {
            this.Debub(ex.getMessage());
        } catch (IOException ex) {
            this.Debub(ex.getMessage());
        }finally {
            //out.close();
            try {
                if (conexion != null) {
                    conexion.Close();
                }
            } catch (SQLException ex) {
                this.Debub(ex.getMessage());
            }

        }

        this.Debub("Cerrando documento");
        try {
            if (document != null) {
                document.close();
            }
        } catch (Exception ex) {
            this.Debub(ex.getMessage());
        }

        this.Debub("Escribiendo archivo");
        outStream.flush();
        outStream.close();
        this.Debub("Proceso terminado");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void BuscarPaginaPDF(String filename, String word, Document document, PdfWriter writer, PdfContentByte cb, String tipo) throws IOException, DocumentException {
        String criterio = "[Cc]onsecutivo [Nn]o\\.\\s{0,}" + word.trim() ;
        this.Debub("Patron de busqueda: " + criterio);
        Pattern pattern = Pattern.compile(criterio);
        //Pattern pf = Pattern.compile("Call.{1,}Center");
        Pattern pf = Pattern.compile("Para[\\s]{1,}mayor[\\s]{1,}informaci[oó]n[\\s]{1,}acerca[\\s]{1,}de[\\s]{1,}esta[\\s]{1,}respuesta");
        
        
        this.Debub("Iniciando busqueda. Proceso " + word);
        
        this.Debub("Archivo de busqueda: " + filename);
        PdfReader reader = new PdfReader(filename);
        this.Debub("Reader abierto");
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        this.Debub("Parser");
        PdfImportedPage page;
        TextExtractionStrategy strategy;
        boolean seguir = false;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new LocationTextExtractionStrategy());
            String texto = strategy.getResultantText();
            Matcher matcher = pattern.matcher(texto);
            if (matcher.find()) {
                this.Debub("Proceso encontrado en la pagina " + i);
                this.numPag++;
                Rectangle rectangle = reader.getPageSizeWithRotation(1);
                document.setPageSize(rectangle);
                document.newPage();
                this.Debub("Creando nueva pagina");

                page = writer.getImportedPage(reader, i);
                switch (rectangle.getRotation()) {
                    case 0:
                        cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                        break;
                    case 90:
                        cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader
                                .getPageSizeWithRotation(1).getHeight());
                        break;
                    case 180:
                        cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                        break;
                    case 270:
                        cb.addTemplate(page, 0, 1.0F, -1.0F, 0, reader
                                .getPageSizeWithRotation(1).getWidth(), 0);
                        break;
                    default:
                        break;
                }
                String cadena = word + "_" + this.numPag;
                PdfContentByte cb2 = writer.getDirectContent();
                cb2.beginText();
                BaseFont bf = BaseFont.createFont();
                cb2.setFontAndSize(bf, 8);
                cb2.setTextMatrix((float) 1, (float) 0, (float) 0, 1, 15, 15);
                cb2.showText(cadena);
                cb2.endText();

                if (tipo.equals("04")) {
                    matcher = pf.matcher(texto);
                    if (!matcher.find()) {
                        seguir = true;
                    }
                }
            } else if (seguir == true) {
                matcher = pf.matcher(texto);
                if (!matcher.find()) {
                    this.Debub("Iniciando busqueda. Proceso " + word + " Fin de documento.");
                    this.numPag++;
                    Rectangle rectangle = reader.getPageSizeWithRotation(1);
                    document.setPageSize(rectangle);
                    document.newPage();
                    this.Debub("Creando nueva pagina. Pagina del documento " + i);

                    page = writer.getImportedPage(reader, i);
                    switch (rectangle.getRotation()) {
                        case 0:
                            cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                            break;
                        case 90:
                            cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader
                                    .getPageSizeWithRotation(1).getHeight());
                            break;
                        case 180:
                            cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                            break;
                        case 270:
                            cb.addTemplate(page, 0, 1.0F, -1.0F, 0, reader
                                    .getPageSizeWithRotation(1).getWidth(), 0);
                            break;
                        default:
                            break;
                    }
                    String cadena = word + "_" + this.numPag;
                    PdfContentByte cb2 = writer.getDirectContent();
                    cb2.beginText();
                    BaseFont bf = BaseFont.createFont();
                    cb2.setFontAndSize(bf, 8);
                    cb2.setTextMatrix((float) 1, (float) 0, (float) 0, 1, 15, 15);
                    cb2.showText(cadena);
                    cb2.endText();
                } else {
                    // Ultima Página con el patrón Call Center
                    this.numPag++;
                    Rectangle rectangle = reader.getPageSizeWithRotation(1);
                    document.setPageSize(rectangle);
                    document.newPage();
                    this.Debub("Creando nueva pagina. Ultima Pagina del documento " + i);

                    page = writer.getImportedPage(reader, i);
                    switch (rectangle.getRotation()) {
                        case 0:
                            cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                            break;
                        case 90:
                            cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader
                                    .getPageSizeWithRotation(1).getHeight());
                            break;
                        case 180:
                            cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                            break;
                        case 270:
                            cb.addTemplate(page, 0, 1.0F, -1.0F, 0, reader
                                    .getPageSizeWithRotation(1).getWidth(), 0);
                            break;
                        default:
                            break;
                    }
                    String cadena = word + "_" + this.numPag;
                    PdfContentByte cb2 = writer.getDirectContent();
                    cb2.beginText();
                    BaseFont bf = BaseFont.createFont();
                    cb2.setFontAndSize(bf, 8);
                    cb2.setTextMatrix((float) 1, (float) 0, (float) 0, 1, 15, 15);
                    cb2.showText(cadena);
                    cb2.endText();

                    break;
                }

            }else {
                this.Debub("Patron no encontrado en la pagina No. " + i);
                //this.Debub(texto);
            }
        }
        writer.freeReader(reader);
        reader.close();

    }

    public void AgregarAnexoPDF(String word, String filename, Document document, PdfWriter writer, PdfContentByte cb) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(filename);
        PdfImportedPage page;
        boolean encontrado = false;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            Rectangle rectangle = reader.getPageSizeWithRotation(1);
            document.setPageSize(rectangle);
            document.newPage();
            this.numPag++;
            page = writer.getImportedPage(reader, i);
            switch (rectangle.getRotation()) {
                case 0:
                    cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                    break;
                case 90:
                    cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader
                            .getPageSizeWithRotation(1).getHeight());
                    break;
                case 180:
                    cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                    break;
                case 270:
                    cb.addTemplate(page, 0, 1.0F, -1.0F, 0, reader
                            .getPageSizeWithRotation(1).getWidth(), 0);
                    break;
                default:
                    break;
            }

            String cadena = word + "_A_" + this.numPag;
            PdfContentByte cb2 = writer.getDirectContent();
            cb2.beginText();
            BaseFont bf = BaseFont.createFont();
            cb2.setFontAndSize(bf, 8);
            cb2.setTextMatrix((float) 1, (float) 0, (float) 0, 1, 15, 15);
            cb2.showText(cadena);
            cb2.endText();
        }
        writer.freeReader(reader);
        reader.close();

    }
    
    public boolean parseTexto(String texto, String word) {
        
        texto = texto.replace("."," ");
        String[] palabras = texto.split(" ");
        int found=0;
        for (int i=0; i < palabras.length; i++) {
            //this.Debub("Palabra [" + i + "] -> " + palabras[i]);
            if (found == 2) {
                this.Debub("Buscando Word: " + word + " Palabra: " + palabras[i]);
            }
            if (palabras[i].trim().toLowerCase().equals("consecutivo")) {
              found = 1;
              this.Debub("found 1 encontrado " + palabras[i]);
            }else if (found == 1 && palabras[i].trim().toLowerCase().equals("no") ) {
                found = 2;
                this.Debub("found 2 encontrado " + palabras[i]);
            }else if (found == 2 && word.trim().contains(palabras[i].trim())) {
                this.Debub("found 3 encontrado " + palabras[i]);
                found = 3;
                break;
            }else {
                found = 0;
            }
        }
        
        return found == 3;
    }

    public class Archivo {

        private String path;
        private String tipo;
        private String localFilename;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getLocalFilename() {
            return localFilename;
        }

        public void setLocalFilename(String localFilename) {
            this.localFilename = localFilename;
        }

    }

    private void Debub(String mensaje) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            java.util.Date hoy = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
            fichero = new FileWriter(file_log, true);
            pw = new PrintWriter(fichero);

            sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");
            pw.println(sdf.format(hoy) + "|" + mensaje);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
