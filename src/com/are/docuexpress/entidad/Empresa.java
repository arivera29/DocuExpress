package com.are.docuexpress.entidad;

public class Empresa {
	private String codigo;
	private String nombre;
	private int estado;
	
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
	public Empresa(String codigo, String nombre, int estado) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.estado = estado;
	}
	public Empresa() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
