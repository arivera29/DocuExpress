/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.ControladorDocumento;
import com.are.docuexpress.controlador.db;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Aimer
 */
@WebServlet("/SrvDownloadFile")
public class SrvDownloadFile extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        String id = (String) request.getParameter("id");
        boolean contador = false;

        if (request.getParameter("contador") != null) {
            String cnt = (String) request.getParameter("contador");
            if (cnt.equals("true")) {
                contador = true;
            }
        }

        db conexion = null;
        boolean encontrado = false;
        boolean anexo = false;
        String filename = "";
        String local_filename = "";
        try {
            conexion = new db();

            String sql = "SELECT id,filename,local_filename,tipo FROM archivo_impresion WHERE id=? ";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, id);
            java.sql.ResultSet rs = conexion.Query(pst);
            if (rs.next()) {
                filename = (String) rs.getString("filename");
                local_filename = (String) rs.getString("local_filename");
                encontrado = true;
                if (rs.getString("tipo").equals("01")) {
                    anexo = true;
                }
                System.out.println("Archivo " + filename + " local: " + local_filename);
            }

            if (encontrado) {
                try {

                    ControladorDocumento controlador = new ControladorDocumento(conexion);

                    ServletContext servletContext = getServletContext();

                    String path = servletContext.getRealPath("/upload") + File.separator + filename;
                    
                    if (!anexo) {
                        this.DownloadFile(path, local_filename, response);
                        //this.generatePDF(path,local_filename,  response);
                    } else {
                        this.generatePDF(path,local_filename,  response);
                    }

                    controlador.addHistorialDescarga(Integer.parseInt(id), (String) session.getAttribute("usuario"));
                    if (contador == true) {
                        controlador.updateContadorDescarga(Integer.parseInt(id));
                    }

                } catch (IOException ex) {
                    Logger.getLogger(SrvDownloadFile.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(SrvDownloadFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error. " + ex.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(SrvDownloadFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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

    private void DownloadFile(String path, String local_filename, HttpServletResponse response) throws IOException {
        File downloadFile = new File(path);
        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", local_filename);
        response.setHeader(headerKey, headerValue);
        FileInputStream inStream = new FileInputStream(downloadFile);
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inStream.close();
        outStream.close();

    }

    private void generatePDF(String path, String local_filename, HttpServletResponse response )
            throws IOException, DocumentException {

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", local_filename);
        response.setHeader(headerKey, headerValue);
        
        OutputStream outStream = response.getOutputStream();
        
        InputStream pdf = new FileInputStream(path);
        
        Document document = new Document();

        int totalPages = 0;

        PdfReader pdfReader = new PdfReader(pdf);
        totalPages += pdfReader.getNumberOfPages();

        PdfWriter writer = PdfWriter.getInstance(document, outStream);

        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfImportedPage page;
        int currentPageNumber = 0;
        int pageOfCurrentReaderPDF = 0;

        while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {

            Rectangle rectangle = pdfReader.getPageSizeWithRotation(1);
            document.setPageSize(rectangle);
            document.newPage();

            pageOfCurrentReaderPDF++;
            currentPageNumber++;

            String header="";
            String[] str = local_filename.split("_");
            if (str.length >=2 ) {
                header = str[0] + "/" + str[1];
            }
            
            page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
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
            
            String cadena = String.format("%s Pag. %s de %s", header, pageOfCurrentReaderPDF, pdfReader.getNumberOfPages());
            //Paragraph p = new Paragraph(cadena, new Font(Font.FontFamily.COURIER, 6));

            PdfContentByte cb2 = writer.getDirectContent();
            cb2.beginText();
            BaseFont bf = BaseFont.createFont();
            cb2.setFontAndSize(bf, 8);
            cb2.setTextMatrix((float)1, (float)0, (float) 0,1, 15, 15);
            cb2.showText(cadena);
            cb2.endText();
            
            

            //p.setAlignment(Element.ALIGN_BOTTOM);
            //document.add(p);

        }
        outStream.flush();
        document.close();
        outStream.close();

    }
}
