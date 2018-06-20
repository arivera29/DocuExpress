/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.servlet;

import com.are.docuexpress.controlador.db;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet("/SrvDownloadGuia")
public class SrvDownloadGuia extends HttpServlet {

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
        try {
            conexion = new db();

            String sql = "SELECT g.filename, d.nic, d.numProceso, d.nroDocumento, d.nomDocumento "
                    + "FROM guias g "
                    + "INNER JOIN documentos d ON d.id = g.IdDocumento  "
                    + "WHERE g.id=? ";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, id);
            java.sql.ResultSet rs = conexion.Query(pst);
            if (rs.next()) {
                filename = (String) rs.getString("filename");
                encontrado = true;
            }

            if (encontrado) {
                try {

                    //ServletContext servletContext = getServletContext();
                    //String path = servletContext.getRealPath("/guias") + File.separator + filename;
                    this.DownloadFile(filename, filename, response);
                        //this.generatePDF(path,local_filename,  response);
                } catch (IOException ex) {
                    Logger.getLogger(SrvDownloadGuia.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error. " + ex.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(SrvDownloadGuia.class.getName()).log(Level.SEVERE, null, ex);
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
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
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

    
}
