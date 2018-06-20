/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.rest;

import com.are.docuexpress.controlador.ControladorAuditoria;
import com.are.docuexpress.controlador.ControladorCausal;
import com.are.docuexpress.controlador.ControladorGuia;
import com.are.docuexpress.controlador.Utilidades;
import com.are.docuexpress.controlador.db;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Aimer
 */
@WebServlet("/GestionGuia")
public class GestionGuia extends HttpServlet {

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
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            Gson gson = new Gson();
            db conexion = null;
            String json="";
            try {

                StringBuilder sb = new StringBuilder();
                BufferedReader br = request.getReader();
                String str = "";
                
                while ((str= br.readLine()) != null) {
                    sb.append(str);
                }
                
                json = sb.toString();
                
                if (json.equals("")) {
                    throw new Exception("Parametro json vacio");
                }
                
                com.are.docuexpress.entidad.JsonGestionGuia guia = gson.fromJson(json, com.are.docuexpress.entidad.JsonGestionGuia.class);
                conexion = new db();
                
                ControladorGuia controlador2 = new ControladorGuia(conexion);
                if (!controlador2.Find(guia.getNumeroGuia())) {
                    throw new Exception("Numero de guia no se encuentra registrado");
                }

                if (!guia.getEstadoGuia().equals("2") && !guia.getEstadoGuia().equals("3")) {
                    throw new Exception("Estado de guia no permitido");
                }
                
                if (guia.getEstadoGuia().equals("3") && guia.getCausalDevolucion().equals("-1")) {
                    throw new Exception("Causal de devolución debe ser defirente de -1");
                }
                if (guia.getEstadoGuia().equals("3")) {
                    ControladorCausal c1 = new ControladorCausal(conexion);
                    if (!c1.find(guia.getCausalDevolucion())) {
                        throw new Exception("Causal de devolución no encontrada");
                    }
                }
                
                
                
                if (guia.getFechaGestion() == null) {
                    throw new Exception("Fecha de gestion de la Guia debe ser diferente de NULL");
                }
                
                ControladorAuditoria c1 = new ControladorAuditoria(conexion);
                if (guia.getEstadoGuia().equals("2")) {
                    c1.Mensajeria(guia.getNumeroGuia(), json, "entrega", "webservice");
                }else {
                    c1.Mensajeria(guia.getNumeroGuia(), json, "devolucion", "webservice");
                }
                
                Date fechaGestion = df.parse(guia.getFechaGestion());
                
                String sql = "UPDATE guias SET estadoGuia=?, fechaGestion=?,usuarioGestion=?,causalDevolucion=?, fechaSistemaGestion = SYSDATETIME() "
                                + " WHERE numeroGuia=?";
                        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                        pst.setString(1, guia.getEstadoGuia());
                        pst.setDate(2, new java.sql.Date(fechaGestion.getTime()));
                        pst.setString(3, guia.getUsuarioGestion());
                        pst.setString(4, guia.getCausalDevolucion());
                        pst.setString(5, guia.getNumeroGuia());
                if (conexion.Update(pst) > 0) {
                    out.print(JsonRespuesta(false, "OK"));
                }

            } catch (JsonSyntaxException | SQLException e) {
                out.print(JsonRespuesta(true, e.getMessage()));
                Utilidades.AgregarLog(e.getMessage() + "\t" + json, "excepciones.txt");
            } catch (Exception ex) {
                out.print(JsonRespuesta(true, ex.getMessage()));
                Utilidades.AgregarLog(ex.getMessage() + "\t" + json, "excepciones.txt");
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        out.print(JsonRespuesta(true, ex.getMessage()));
                        Utilidades.AgregarLog(ex.getMessage() + "\t" + json, "excepciones.txt");
                    }
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

    private class Response {

        private boolean error;
        private String mensaje;

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

    }

    private String JsonRespuesta(boolean error, String mensaje) {
        Gson gson = new Gson();
        Response resp = new Response();
        resp.error = error;
        resp.mensaje = mensaje;
        String respuesta = gson.toJson(resp);
        return respuesta;
    }
}
