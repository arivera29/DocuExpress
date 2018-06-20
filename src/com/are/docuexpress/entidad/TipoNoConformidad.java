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
public class TipoNoConformidad {
    private String codigo;
    private String nombre;
    private int estado;
    private String correoNotificacion1;
    private String correoNotificacion2;
    private int nivelCriticidad;
    private int responsable;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCorreoNotificacion1() {
        return correoNotificacion1;
    }

    public void setCorreoNotificacion1(String correoNotificacion1) {
        this.correoNotificacion1 = correoNotificacion1;
    }

    public String getCorreoNotificacion2() {
        return correoNotificacion2;
    }

    public void setCorreoNotificacion2(String correoNotificacion2) {
        this.correoNotificacion2 = correoNotificacion2;
    }

    public int getNivelCriticidad() {
        return nivelCriticidad;
    }

    public void setNivelCriticidad(int nivelCriticidad) {
        this.nivelCriticidad = nivelCriticidad;
    }

    public int getResponsable() {
        return responsable;
    }

    public void setResponsable(int responsable) {
        this.responsable = responsable;
    }
    
    
    
}
