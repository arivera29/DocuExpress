package com.are.docuexpress.controlador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.are.docuexpress.entidad.Perfiles;


public class ControladorPerfil{
	
	private Perfiles perfil;
	private db conexion = null;

	public ControladorPerfil(db conexion) {
		this.conexion = conexion;
		// TODO Auto-generated constructor stub
	}


	public Perfiles getPerfil() {
		return perfil;
	}



	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}

	public boolean add() throws SQLException {
		boolean result = false;
		String sql = "insert into perfiles (perfil) values (?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, perfil.getPerfil());
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}

	public boolean remove(String id) throws SQLException {
		boolean result = false;
		String sql = "delete from perfiles where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, id);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
			
		}
		return result;
	}

	public boolean update(String id) throws SQLException {
		boolean result = false;
		String sql = "update perfiles set perfil=? where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1,perfil.getPerfil() );
		pst.setString(2, id);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public ArrayList<Perfiles> list() throws SQLException {
		ArrayList<Perfiles> lista = new ArrayList<Perfiles>();
		String sql = "select * from perfiles order by perfil";
		ResultSet rs = conexion.Query(sql);
		while (rs.next()) {
			Perfiles perfil = new Perfiles();
			perfil.setId(rs.getString("id"));
			perfil.setPerfil(rs.getString("perfil"));
			lista.add(perfil);
		}
		rs.close();
		return lista;
	}
	
	public boolean Find(String id) throws SQLException {
		boolean result = false;
		String sql = "select * from perfiles where id = ?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1,id);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			perfil = new Perfiles();
			perfil.setId(rs.getString("id"));
			perfil.setPerfil(rs.getString("perfil"));
			result = true;
		}
		rs.close();
		
		return result;
		
	}
	
	public boolean  AllowAccessPage(String perfil, String rol) throws SQLException {
		boolean result = false;
		String sql = "select perfiles_menu.id from perfiles_menu, menu where  perfiles_menu.id_perfil = ? and perfiles_menu.id_menu = menu.menuid and menu.rol = ?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1,perfil);
		pst.setString(2,rol);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		return result;
	}
	
	
}
