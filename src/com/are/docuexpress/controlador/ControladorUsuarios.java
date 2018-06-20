package com.are.docuexpress.controlador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.are.docuexpress.entidad.Usuarios;
import com.are.docuexpress.entidad.Zonas;


public class ControladorUsuarios {

	private db conexion;
	private Usuarios usuario;

	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Usuarios getUsuario() {
		return usuario;
	}
	
	public ControladorUsuarios(db conexion) {
		super();
		this.conexion = conexion;
	}

	public boolean add(Usuarios usuario) throws SQLException{
		boolean result = false;
			String sql = "INSERT INTO usuarios (usuario,nombre,perfil,clave,estado) "
					+ "VALUES (?,?,?,CONVERT(VARCHAR(32), HashBytes('MD5', '" + usuario.getClave() + "'), 2),?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, usuario.getUsuario());
			pst.setString(2, usuario.getNombre());
			pst.setString(3, usuario.getPerfil());
			pst.setString(4, usuario.getEstado());
		
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;
	}
	
	public boolean update(Usuarios usuario,String key) throws SQLException{
		boolean result = false;
			String sql = "UPDATE usuarios SET " +
					"usuario=?," +
					"nombre=?," +
					"perfil=?," +
					"estado=?" ;
			if (!usuario.getClave().equals("")) {
				sql += ",clave=CONVERT(VARCHAR(32), HashBytes('MD5','" + usuario.getClave() + "'), 2) ";
			}
			
			sql += " WHERE usuario=?";
			
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, usuario.getUsuario());
			pst.setString(2, usuario.getNombre());
			pst.setString(3, usuario.getPerfil());
			pst.setString(4, usuario.getEstado());
			pst.setString(5,key);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;

			String sql = "DELETE FROM usuarios WHERE usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean exist(String key) throws SQLException {
		boolean result = false;

			String sql = "SELECT usuario, nombre, estado, clave,perfil "
						+ "FROM usuarios "
						+ "WHERE usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,key);
			this.usuario = null;
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				result = true;
			}
			rs.close();
		return result;	
	}
	
	public boolean find(String key) throws SQLException {
		boolean result = false;

			String sql = "SELECT usuario, nombre, estado, clave,perfil "
						+ "FROM usuarios "
						+ "WHERE usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,key);
			usuario = new Usuarios();
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				result = true;
			}
			rs.close();
		return result;	
	}
		
	public ArrayList<Usuarios> list() throws SQLException {
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
			String sql = "SELECT usuario, nombre, estado, clave,perfil "
					+ "FROM usuarios "
					+ "ORDER BY usuario";
			ResultSet rs = conexion.Query(sql);
			while(rs.next()) {
				Usuarios usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				lista.add(usuario);
			}
		return lista;	
	}
	
	public ArrayList<Zonas> listZonas(String usuario) throws SQLException {
		ArrayList<Zonas> lista = new ArrayList<Zonas>();
		String sql = "select zonas.* "
				+ "FROM zonas,zonas_usuario "
				+ "WHERE zonas.zonacodi=zonas_usuario.zouszona "
				+ " AND zonas_usuario.zoususua=?"
				+ "order by zonanomb";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, usuario);
		ResultSet rs = conexion.Query(pst);
		while(rs.next()) {
			Zonas zona = new Zonas(rs.getInt("zonacodi"),rs.getString("zonanomb"));
			lista.add(zona);
		}
		
		return lista;
	}
	
	public ArrayList<Usuarios> filter(String criterio) throws SQLException {
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
			String sql = "SELECT usuario, nombre, estado, clave,perfil "
					+ "FROM usuarios "
					+ "WHERE usuario like ? OR nombre like ?"
					+ "ORDER BY usuario";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,"%" + criterio + "%");
			pst.setString(2,"%" + criterio + "%");
			
			ResultSet rs = conexion.Query(pst);
			while(rs.next()) {
				Usuarios usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				lista.add(usuario);
			}
		return lista;	
	}
		
	public boolean login(String user, String clave) throws SQLException {
		boolean ret = false;

			String sql = "SELECT usuario, nombre, estado, clave,perfil "
					+ "FROM usuarios "
					+ "WHERE usuario=? and estado=1 "
					+ "AND clave=CONVERT(VARCHAR(32), HashBytes('MD5', '" + clave + "'), 2)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,user);
			//pst.setString(2,clave);
			System.out.println("Usuario:" + user + ", clave:" + clave);
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				System.out.println("Usuario valido");
				usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				ret = true;
			}else {
				System.out.println("Usuario No valido");
			}
			
		return ret;
	}
	
	public boolean changepassword(String user,String clave) throws SQLException {
		boolean ret = false;

			String sql = "update usuarios set clave=CONVERT(VARCHAR(32), HashBytes('MD5', ?), 2) where usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,clave);
			pst.setString(2,user);
			
			if (conexion.Update(pst)>0) {
				conexion.Commit();
				ret = true;
			}
		return ret;
	}
	
	public boolean addZona(int id, String usuario) throws SQLException {
		boolean result = false;
		String sql = "insert into zonas_usuario (zouszona,zoususua) values (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setInt(1, id);
		pst.setString(2, usuario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean removeZona(int id, String usuario) throws SQLException {
		boolean result = false;
		String sql = "DELETE FROM zonas_usuario WHERE zouszona=? AND zoususua=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setInt(1, id);
		pst.setString(2, usuario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}

}
