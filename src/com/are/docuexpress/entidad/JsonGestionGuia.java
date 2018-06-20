/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.entidad;

/**
 *
 * @author Aimer
 */
public class JsonGestionGuia {
    private String numeroGuia;
    private String estadoGuia;
    private String fechaGestion;
    private String causalDevolucion;
    private String usuarioGestion;

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getEstadoGuia() {
        return estadoGuia;
    }

    public void setEstadoGuia(String estadoGuia) {
        this.estadoGuia = estadoGuia;
    }

    public String getFechaGestion() {
        return fechaGestion;
    }

    public void setFechaGestion(String fechaGestion) {
        this.fechaGestion = fechaGestion;
    }

    public String getCausalDevolucion() {
        return causalDevolucion;
    }

    public void setCausalDevolucion(String causalDevolucion) {
        this.causalDevolucion = causalDevolucion;
    }

    public String getUsuarioGestion() {
        return usuarioGestion;
    }

    public void setUsuarioGestion(String usuarioGestion) {
        this.usuarioGestion = usuarioGestion;
    }

    public JsonGestionGuia() {
        this.numeroGuia = "";
        this.estadoGuia = "";
        this.fechaGestion = "";
        this.causalDevolucion = "";
        this.usuarioGestion = "";
    }

    public JsonGestionGuia(String numeroGuia, String estadoGuia, String fechaGestion, String causalDevolucion, String usuarioGestion) {
        this.numeroGuia = numeroGuia;
        this.estadoGuia = estadoGuia;
        this.fechaGestion = fechaGestion;
        this.causalDevolucion = causalDevolucion;
        this.usuarioGestion = usuarioGestion;
    }
    
    
    
}
