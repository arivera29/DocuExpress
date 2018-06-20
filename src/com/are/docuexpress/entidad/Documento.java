package com.are.docuexpress.entidad;

public class Documento {
    private int identificador;  // Identificador único registro
    private String nic;
    private String numero;
    private String nombre;
    private String municipio;
    private String barrio;
    private String via;
    private String crucero;
    private String placa;
    private String reclamante;
    private String telefono;
    private String proceso;
    private String codigo;
    private int idCarga; // Identificador relacionado registro de carga
    private String fechaCarga;
    private String usuarioCarga;
    private int estado;
    private int rowFile;
    private String fechaEntrega;
    
    public Documento() {
        this.identificador=0;
        this.nic="";
        this.numero="";
        this.nombre="";
        this.municipio="";
        this.barrio="";
        this.via="";
        this.crucero="";
        this.placa="";
        this.reclamante="";
        this.telefono="";
        this.proceso="";
        this.codigo="";
        this.idCarga=-1;
        this.usuarioCarga="";
        this.fechaCarga="";
        this.estado=-1;
        this.rowFile = 0;
        this.fechaEntrega="";
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCrucero() {
        return crucero;
    }

    public void setCrucero(String crucero) {
        this.crucero = crucero;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getReclamante() {
        return reclamante;
    }

    public void setReclamante(String reclamante) {
        this.reclamante = reclamante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public int getIdCarga() {
        return idCarga;
    }

    public void setIdCarga(int idCarga) {
        this.idCarga = idCarga;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getUsuarioCarga() {
        return usuarioCarga;
    }

    public void setUsuarioCarga(String usuarioCarga) {
        this.usuarioCarga = usuarioCarga;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getRowFile() {
        return rowFile;
    }

    public void setRowFile(int rowFile) {
        this.rowFile = rowFile;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

}
