/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.db;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "SrvDescargarAll", urlPatterns = {"/SrvDescargarAll"})
public class SrvDescargarAll extends HttpServlet {

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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletContext servletContext = getServletContext();
        path = servletContext.getRealPath("/log") + File.separator;
        try {
            String id = (String) request.getParameter("id");
            db conexion = null;
            String sql = "SELECT filename,local_filename,tipo "
                    + " FROM archivo_impresion "
                    + " INNER JOIN TipoArchivo ON archivo_impresion.tipo = TipoArchivo.codTipoArchivo "
                    + " WHERE id_archivo = ? AND estado=1  "
                    + " ORDER BY TipoArchivo.ordTipoArchivo ";

            try {
                String rutaApp = servletContext.getRealPath("/upload");

                conexion = new db();
                List<Archivo> archivos = new ArrayList<Archivo>();
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, id);
                java.sql.ResultSet rs = conexion.Query(pst);
                this.Debub("Archivos a procesar...");
                while (rs.next()) {
                    String path = rs.getString("filename");
                    path = rutaApp + File.separator + path;
                    File file = new File(path);
                    if (file.exists()) {
                        this.Debub("Path archivo " + path + " Local filename: " + rs.getString("local_filename") );
                        Archivo arc = new Archivo();
                        arc.setArchivo(new FileInputStream(path));
                        arc.setFilename(rs.getString("local_filename"));
                        arc.setTipo(rs.getString("tipo"));
                        archivos.add(arc);
                    }
                }

                if (archivos.size() > 0) {
                    String mimeType = "application/octet-stream";
                    response.setContentType(mimeType);
                    String headerKey = "Content-Disposition";
                    String fname = "documentos.pdf";
                    String headerValue = String.format("attachment; filename=\"%s\"", fname);
                    response.setHeader(headerKey, headerValue);
                    generatePDF(archivos, response);
                } else {
                    PrintWriter out = response.getWriter();
                    out.println("No se encontraron archivos con el ID " + id);
                }

            } catch (SQLException | DocumentException ex) {
                Logger.getLogger(SrvDescargarAll.class.getName()).log(Level.SEVERE, null, ex);
                this.Debub(ex.getMessage());
            }

        } finally {

        }
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

    private void generatePDF(List<Archivo> archivos, HttpServletResponse response)
            throws IOException, DocumentException {
        Document document = new Document();
        
        List<Archivo> pdfs = archivos;
        List<PdfReader> readers = new ArrayList<PdfReader>();
        int totalPages = 0;
        Iterator<Archivo> iteratorPDFs = pdfs.iterator();
        UUID identificador = UUID.randomUUID();
        String filename = identificador + ".pdf";
        OutputStream outputStream = response.getOutputStream();

        while (iteratorPDFs.hasNext()) {
            
            Archivo actual = iteratorPDFs.next();
            this.Debub("Leyendo archivo " + actual.getFilename());
            InputStream pdf = actual.getArchivo();
            PdfReader pdfReader = new PdfReader(pdf);
            actual.setReader(pdfReader);
            //readers.add(pdfReader);
            totalPages += pdfReader.getNumberOfPages();
        }

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfImportedPage page;
        int currentPageNumber = 0;
        int pageOfCurrentReaderPDF = 0;
        //Iterator<PdfReader> iteratorPDFReader = readers.iterator();
        iteratorPDFs = pdfs.iterator();

        while (iteratorPDFs.hasNext()) {
            Archivo actual = iteratorPDFs.next();
            PdfReader pdfReader = actual.getReader();
            this.Debub("Uniendo archivo " + actual.getFilename());

            while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {

                pageOfCurrentReaderPDF++;
                currentPageNumber++;

                Rectangle rectangle = pdfReader.getPageSizeWithRotation(1);
                document.setPageSize(rectangle);
                document.newPage();
                

                page = writer.getImportedPage(pdfReader,
                        pageOfCurrentReaderPDF);

                switch (rectangle.getRotation()) {
                    case 0:
                        cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                        break;
                    case 90:
                        cb.addTemplate(page, 0, -1f, 1f, 0, 0, pdfReader
                                .getPageSizeWithRotation(1).getHeight());
                        break;
                    case 180:
                        cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                        break;
                    case 270:
                        cb.addTemplate(page, 0, 1.0F, -1.0F, 0, pdfReader
                                .getPageSizeWithRotation(1).getWidth(), 0);
                        break;
                    default:
                        break;
                }
                
                if (actual.getTipo().equals("01")) {
                    String fname = actual.getFilename();
                    String header="";
                    String[] str = fname.split("_");
                    if (str.length >=2 ) {
                        header = str[0] + "/" + str[1];
                    }
                    String cadena = String.format("%s Pag. %s de %s", header, pageOfCurrentReaderPDF, pdfReader.getNumberOfPages());
                    //Paragraph p = new Paragraph(cadena, new Font(Font.FontFamily.COURIER, 6));

                    PdfContentByte cb2 = writer.getDirectContent();
                    cb2.beginText();
                    BaseFont bf = BaseFont.createFont();
                    cb2.setFontAndSize(bf, 8);
                    cb2.setTextMatrix((float)1, (float)0, (float) 0,1, 15, 15);
                    cb2.showText(cadena);
                    cb2.endText();

                }

            }
            pageOfCurrentReaderPDF = 0;
        }
        outputStream.flush();
        document.close();
        outputStream.close();

    }
    
    private void Debub(String mensaje) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            java.util.Date hoy = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");
            fichero = new FileWriter(this.path + "debug.txt", true);
            pw = new PrintWriter(fichero);

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

    public class Archivo {

        private InputStream archivo;
        private String tipo;
        private String filename;
        private PdfReader reader;

        public InputStream getArchivo() {
            return archivo;
        }

        public void setArchivo(InputStream archivo) {
            this.archivo = archivo;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public PdfReader getReader() {
            return reader;
        }

        public void setReader(PdfReader reader) {
            this.reader = reader;
        }

    }
}
