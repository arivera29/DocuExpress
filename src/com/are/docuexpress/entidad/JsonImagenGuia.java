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
public class JsonImagenGuia {
    private String numeroGuia;
    private String jsonImagen;
    private String usuarioCarga;

    public JsonImagenGuia(String numeroGuia, String jsonImagen) {
        this.numeroGuia = numeroGuia;
        this.jsonImagen = jsonImagen;
    }

    public JsonImagenGuia() {
        this.numeroGuia = "";
        this.jsonImagen = "";
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getJsonImagen() {
        return jsonImagen;
    }

    public void setJsonImagen(String jsonImagen) {
        this.jsonImagen = jsonImagen;
    }

    public String getUsuarioCarga() {
        return usuarioCarga;
    }

    public void setUsuarioCarga(String usuarioCarga) {
        this.usuarioCarga = usuarioCarga;
    }
    
    
}
