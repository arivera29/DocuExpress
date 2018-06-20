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
public class JsonGuia {
   private String idDocumento;
   private String numeroGuia;
   private String fechaCreacion;
   private String fechaGeneracion;
   private String direccion;
   private String usuarioCreacion;

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public JsonGuia() {
        this.idDocumento = "";
        this.numeroGuia = "";
        this.fechaCreacion = "";
        this.direccion = "";
        this.usuarioCreacion = "";
    }

    public JsonGuia(String idDocumento, String numeroGuia, String fechaCreacion, String direccion, String usuarioCreacion) {
        this.idDocumento = idDocumento;
        this.numeroGuia = numeroGuia;
        this.fechaCreacion = fechaCreacion;
        this.direccion = direccion;
        this.usuarioCreacion = usuarioCreacion;
    }
    
    
}
