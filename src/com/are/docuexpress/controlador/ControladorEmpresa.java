package com.are.docuexpress.controlador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.are.docuexpress.entidad.Empresa;

public class ControladorEmpresa {
	private db conexion;
	private Empresa empresa;
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public ControladorEmpresa(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean add(Empresa empresa) throws SQLException{
		boolean result = false;
			String sql = "INSERT INTO empresa (codEmpresa,nomEmpresa,estEmpresa) VALUES (?,?,?)";
			
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, empresa.getCodigo());
			pst.setString(2, empresa.getNombre());
			pst.setInt(3, empresa.getEstado());
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}

	public boolean update(Empresa empresa,String key) throws SQLException {
		boolean result = false;

		String sql = "UPDATE empresa SET codEmpresa=?,nomEmpresa=?, estEmpresa=? WHERE codEmpresa=?";
		
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, empresa.getCodigo());
		pst.setString(2, empresa.getNombre());
		pst.setInt(3, empresa.getEstado());
		pst.setString(4, key);
		
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;
			String sql = "DELETE FROM empresa WHERE codEmpresa=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		    this.empresa = null;
			String sql = "SELECT codEmpresa,nomEmpresa,estEmpresa FROM empresa WHERE codEmpresa=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
                            empresa = new Empresa();
                            empresa.setCodigo((String)rs.getString("codEmpresa"));
                            empresa.setNombre((String)rs.getString("nomEmpresa"));
                            empresa.setEstado(rs.getInt("estEmpresa"));
                            result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ArrayList<Empresa> List() throws SQLException {
		ArrayList<Empresa> lista = new ArrayList<Empresa>();
		String sql = "SELECT codEmpresa,nomEmpresa,estEmpresa FROM empresa ORDER BY codEmpresa";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Empresa empresa = new Empresa();
			empresa.setCodigo((String)rs.getString("codEmpresa"));
			empresa.setNombre((String)rs.getString("nomEmpresa"));
			empresa.setEstado(rs.getInt("estEmpresa"));
			lista.add(empresa);
		}
		
		return lista;
	}
	
	public ArrayList<Empresa> List(int estado) throws SQLException {
		ArrayList<Empresa> lista = new ArrayList<Empresa>();
		String sql = "SELECT codEmpresa,nomEmpresa,estEmpresa FROM empresa WHERE estEmpresa=? ORDER BY nomEmpresa";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, estado);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Empresa empresa = new Empresa();
			empresa.setCodigo((String)rs.getString("codEmpresa"));
			empresa.setNombre((String)rs.getString("nomEmpresa"));
			empresa.setEstado(rs.getInt("estEmpresa"));
			lista.add(empresa);
		}
		
		return lista;
	}
	
	

}
