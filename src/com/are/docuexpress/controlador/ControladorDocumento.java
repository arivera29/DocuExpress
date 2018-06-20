package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.Documento;
import com.are.docuexpress.entidad.JsonGuiaPendiente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorDocumento {

    private db conex;
    private Documento documento;

    public db getConex() {
        return conex;
    }

    public void setConex(db conex) {
        this.conex = conex;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public ControladorDocumento(db conex, Documento documento) {
        super();
        this.conex = conex;
        this.documento = documento;
    }

    public ControladorDocumento(db conex) {
        super();
        this.conex = conex;
    }

    public boolean Add(Documento documento) throws SQLException {
        boolean resultado = false;
        String sql = "INSERT INTO Documentos (nic,nroDocumento,nomDocumento,municipio,barrio,via,crucero,placa,nomReclamante,telefono,numProceso,fechaCarga,usuarioCarga,idCarga,estDocumento,codDocumento,rowFile,fechaEntrega)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,SYSDATETIME(),?,?,?,?,?,?)";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, documento.getNic());
        pst.setString(2, documento.getNumero());
        pst.setString(3, documento.getNombre());
        pst.setString(4, documento.getMunicipio());
        pst.setString(5, documento.getBarrio());
        pst.setString(6, documento.getVia());
        pst.setString(7, documento.getCrucero());
        pst.setString(8, documento.getPlaca());
        pst.setString(9, documento.getReclamante());
        pst.setString(10, documento.getTelefono());
        pst.setString(11, documento.getProceso());
        pst.setString(12, documento.getUsuarioCarga());
        pst.setInt(13, documento.getIdCarga());
        pst.setInt(14, documento.getEstado());
        pst.setString(15, documento.getCodigo());
        pst.setInt(16, documento.getRowFile());
        pst.setString(17, documento.getFechaEntrega());
        if (conex.Update(pst) > 0) {
            conex.Commit();
            resultado = true;
        }
        return resultado;
    }
    
    public boolean Add(Documento documento, boolean commit) throws SQLException {
        boolean resultado = false;
        String sql = "INSERT INTO Documentos (nic,nroDocumento,nomDocumento,municipio,barrio,via,crucero,placa,nomReclamante,telefono,numProceso,fechaCarga,usuarioCarga,idCarga,estDocumento,codDocumento,rowFile)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,SYSDATETIME(),?,?,?,?,?)";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, documento.getNic());
        pst.setString(2, documento.getNumero());
        pst.setString(3, documento.getNombre());
        pst.setString(4, documento.getMunicipio());
        pst.setString(5, documento.getBarrio());
        pst.setString(6, documento.getVia());
        pst.setString(7, documento.getCrucero());
        pst.setString(8, documento.getPlaca());
        pst.setString(9, documento.getReclamante());
        pst.setString(10, documento.getTelefono());
        pst.setString(11, documento.getProceso());
        pst.setString(12, documento.getUsuarioCarga());
        pst.setInt(13, documento.getIdCarga());
        pst.setInt(14, documento.getEstado());
        pst.setString(15, documento.getCodigo());
        pst.setInt(16, documento.getRowFile());
        if (conex.Update(pst) > 0) {
            if (commit) {
                conex.Commit();
            }
            resultado = true;
        }
        return resultado;
    }

    public boolean Update(Documento documento, int identificador) throws SQLException {
        boolean resultado = false;
        String sql = "UPDATE Documentos SET nic=?,nroDocumento=?,"
                + "nomDocumento=?,municipio=?,barrio=?,via=?,"
                + "crucero=?,placa=?,nomReclamante=?,telefono=?,"
                + "numProceso=?,estDocumento=?, codDocumento=?, fechaEntrega=? WHERE id=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, documento.getNic());
        pst.setString(2, documento.getNumero());
        pst.setString(3, documento.getNombre());
        pst.setString(4, documento.getUsuarioCarga());
        pst.setString(5, documento.getMunicipio());
        pst.setString(6, documento.getBarrio());
        pst.setString(7, documento.getVia());
        pst.setString(8, documento.getCrucero());
        pst.setString(9, documento.getPlaca());
        pst.setString(10, documento.getReclamante());
        pst.setString(11, documento.getTelefono());
        pst.setString(12, documento.getProceso());
        pst.setInt(13, documento.getEstado());
        pst.setString(14, documento.getCodigo());
        pst.setString(15, documento.getFechaEntrega());
        pst.setInt(16, identificador);

        if (conex.Update(pst) > 0) {
            conex.Commit();
            resultado = true;
        }
        return resultado;
    }
    
    public boolean Remove(int identificador) throws SQLException {
        boolean resultado = false;
        String sql = "DELETE FROM WHERE id=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setInt(1, identificador);

        if (conex.Update(pst) > 0) {
            conex.Commit();
            resultado = true;
        }
        return resultado;
    }

    public boolean Find(int identificador) throws SQLException {
        boolean encontrado = false;
        String sql = "SELECT * FROM documentos WHERE id=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setInt(1, identificador);
        ResultSet rs = conex.Query(pst);
        if (rs.next()) {

            documento = new Documento();
            documento.setIdentificador(rs.getInt("id"));
            documento.setNic(rs.getString("nic"));
            documento.setNumero(rs.getString("nroDocumento"));
            documento.setNombre(rs.getString("nomDocumento"));
            documento.setMunicipio(rs.getString("municipio"));
            documento.setBarrio(rs.getString("barrio"));
            documento.setVia(rs.getString("via"));
            documento.setCrucero(rs.getString("crucero"));
            documento.setPlaca(rs.getString("placa"));
            documento.setReclamante(rs.getString("nomReclamante"));
            documento.setProceso(rs.getString("numProceso"));
            documento.setIdCarga(rs.getInt("idCarga"));
            documento.setTelefono(rs.getString("telefono"));
            documento.setFechaCarga(rs.getString("fechaCarga"));
            documento.setUsuarioCarga(rs.getString("usuarioCarga"));
            documento.setEstado(rs.getInt("estDocumento"));
            documento.setCodigo(rs.getString("codDocumento"));
            documento.setRowFile(rs.getInt("rowFile"));
            encontrado = true;
        }
        return encontrado;
    }
    
    public ArrayList<Documento> list(int idCarga) throws SQLException {
        ArrayList<Documento> lista = new ArrayList<Documento>();
        String sql = "SELECT * FROM documentos WHERE idCarga=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setInt(1, idCarga);
        ResultSet rs = conex.Query(pst);
        while (rs.next()) {
            Documento documento = new Documento();
            documento.setIdentificador(rs.getInt("id"));
            documento.setNic(rs.getString("nic"));
            documento.setNumero(rs.getString("nroDocumento"));
            documento.setNombre(rs.getString("nomDocumento"));
            documento.setMunicipio(rs.getString("municipio"));
            documento.setBarrio(rs.getString("barrio"));
            documento.setVia(rs.getString("via"));
            documento.setCrucero(rs.getString("crucero"));
            documento.setPlaca(rs.getString("placa"));
            documento.setReclamante(rs.getString("nomReclamante"));
            documento.setProceso(rs.getString("numProceso"));
            documento.setIdCarga(rs.getInt("idCarga"));
            documento.setTelefono(rs.getString("telefono"));
            documento.setFechaCarga(rs.getString("fechaCarga"));
            documento.setUsuarioCarga(rs.getString("usuarioCarga"));
            documento.setEstado(rs.getInt("estDocumento"));
            documento.setCodigo(rs.getString("codDocumento"));
            documento.setRowFile(rs.getInt("rowFile"));
            
            lista.add(documento);
        } 
        
        return lista;
        
    }
    
    public ArrayList<Documento> FindByNic(String nic) throws SQLException {
        ArrayList<Documento> lista = new ArrayList<Documento>();
        String sql = "SELECT * FROM documentos WHERE nic=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, nic);
        ResultSet rs = conex.Query(pst);
        while (rs.next()) {
            Documento documento = new Documento();
            documento.setIdentificador(rs.getInt("id"));
            documento.setNic(rs.getString("nic"));
            documento.setNumero(rs.getString("nroDocumento"));
            documento.setNombre(rs.getString("nomDocumento"));
            documento.setMunicipio(rs.getString("municipio"));
            documento.setBarrio(rs.getString("barrio"));
            documento.setVia(rs.getString("via"));
            documento.setCrucero(rs.getString("crucero"));
            documento.setPlaca(rs.getString("placa"));
            documento.setReclamante(rs.getString("nomReclamante"));
            documento.setProceso(rs.getString("numProceso"));
            documento.setIdCarga(rs.getInt("idCarga"));
            documento.setTelefono(rs.getString("telefono"));
            documento.setFechaCarga(rs.getString("fechaCarga"));
            documento.setUsuarioCarga(rs.getString("usuarioCarga"));
            documento.setEstado(rs.getInt("estDocumento"));
            documento.setCodigo(rs.getString("codDocumento"));
            documento.setRowFile(rs.getInt("rowFile"));
            
            lista.add(documento);
        } 
        
        return lista;
        
    }
    
    public ArrayList<Documento> FindByNumero(String numero) throws SQLException {
        ArrayList<Documento> lista = new ArrayList<Documento>();
        String sql = "SELECT * FROM documentos WHERE numDocumento=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, numero);
        ResultSet rs = conex.Query(pst);
        while (rs.next()) {
            Documento documento = new Documento();
            documento.setIdentificador(rs.getInt("id"));
            documento.setNic(rs.getString("nic"));
            documento.setNumero(rs.getString("nroDocumento"));
            documento.setNombre(rs.getString("nomDocumento"));
            documento.setMunicipio(rs.getString("municipio"));
            documento.setBarrio(rs.getString("barrio"));
            documento.setVia(rs.getString("via"));
            documento.setCrucero(rs.getString("crucero"));
            documento.setPlaca(rs.getString("placa"));
            documento.setReclamante(rs.getString("nomReclamante"));
            documento.setProceso(rs.getString("numProceso"));
            documento.setIdCarga(rs.getInt("idCarga"));
            documento.setTelefono(rs.getString("telefono"));
            documento.setFechaCarga(rs.getString("fechaCarga"));
            documento.setUsuarioCarga(rs.getString("usuarioCarga"));
            documento.setEstado(rs.getInt("estDocumento"));
            documento.setCodigo(rs.getString("codDocumento"));
            documento.setRowFile(rs.getInt("rowFile"));
            
            lista.add(documento);
        } 
        
        return lista;
        
    }
    
    public ArrayList<Documento> FindByProceso(String proceso) throws SQLException {
        ArrayList<Documento> lista = new ArrayList<Documento>();
        String sql = "SELECT * FROM documentos WHERE numProceso=?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setString(1, proceso);
        ResultSet rs = conex.Query(pst);
        while (rs.next()) {
            Documento documento = new Documento();
            documento.setIdentificador(rs.getInt("id"));
            documento.setNic(rs.getString("nic"));
            documento.setNumero(rs.getString("nroDocumento"));
            documento.setNombre(rs.getString("nomDocumento"));
            documento.setMunicipio(rs.getString("municipio"));
            documento.setBarrio(rs.getString("barrio"));
            documento.setVia(rs.getString("via"));
            documento.setCrucero(rs.getString("crucero"));
            documento.setPlaca(rs.getString("placa"));
            documento.setReclamante(rs.getString("nomReclamante"));
            documento.setProceso(rs.getString("numProceso"));
            documento.setIdCarga(rs.getInt("idCarga"));
            documento.setTelefono(rs.getString("telefono"));
            documento.setFechaCarga(rs.getString("fechaCarga"));
            documento.setUsuarioCarga(rs.getString("usuarioCarga"));
            documento.setEstado(rs.getInt("estDocumento"));
            documento.setCodigo(rs.getString("codDocumento"));
            documento.setRowFile(rs.getInt("rowFile"));
            
            lista.add(documento);
        } 
        
        return lista;
        
    }
    
    public ArrayList<Documento> PendienteConfirmarGuia() throws SQLException {
        ArrayList<Documento> lista = new ArrayList<Documento>();
        String sql = "SELECT TOP(40000) * FROM documentos INNER JOIN TipoDocumento t ON documentos.nomDocumento=t.codTipoDocumento WHERE id not in (select DISTINCT idDocumento FROM guias ) ORDER BY fechaCarga DESC";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        ResultSet rs = conex.Query(pst);
        while (rs.next()) {
            Documento documento = new Documento();
            documento.setIdentificador(rs.getInt("id"));
            documento.setNic(rs.getString("nic"));
            documento.setNumero(rs.getString("nroDocumento"));
            documento.setNombre(rs.getString("nomTipoDocumento"));
            documento.setMunicipio(rs.getString("municipio"));
            documento.setBarrio(rs.getString("barrio"));
            documento.setVia(rs.getString("via"));
            documento.setCrucero(rs.getString("crucero"));
            documento.setPlaca(rs.getString("placa"));
            documento.setReclamante(rs.getString("nomReclamante"));
            documento.setProceso(rs.getString("numProceso"));
            documento.setIdCarga(rs.getInt("idCarga"));
            documento.setTelefono(rs.getString("telefono"));
            documento.setFechaCarga(rs.getString("fechaCarga"));
            documento.setUsuarioCarga(rs.getString("usuarioCarga"));
            documento.setEstado(rs.getInt("estDocumento"));
            documento.setCodigo(rs.getString("codDocumento"));
            documento.setRowFile(rs.getInt("rowFile"));
            lista.add(documento);
        } 
        
        return lista;
        
    }
    
    public ArrayList<JsonGuiaPendiente> PendienteGestion() throws SQLException {
        ArrayList<JsonGuiaPendiente> lista = new ArrayList<JsonGuiaPendiente>();
        String sql = "SELECT estadoGuia,uploadImagen,numeroGuia FROM guias WHERE estadoGuia = 1 OR uploadImagen=0";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        ResultSet rs = conex.Query(pst);
        while (rs.next()) {
            JsonGuiaPendiente json = new JsonGuiaPendiente();
            json.setGuia(rs.getString("numeroGuia"));
            int gestion = rs.getInt("estadoGuia");
            int imagen = rs.getInt("uploadImagen");
            if (gestion == 1 && imagen == 0) {  // falta gestion e imagen
                json.setFlag(3);
            }else if (gestion == 1 && imagen == 1) {  // falta gestion
                json.setFlag(1);
            }else if (gestion != 1 && imagen == 0 ) { // falta imagen
                json.setFlag(2);
            }else {
                json.setFlag(3);  // falta gestion e imagen
            }
            lista.add(json);
        } 
        
        return lista;
        
    }
    
    public boolean addHistorialDescarga(int id, String usuario) throws SQLException {
        boolean resultado = false;
        String sql = "INSERT INTO historial_descargas (id_doc_impresion,usuario,fecha) VALUES (?,?,SYSDATETIME())";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setInt(1, id);
        pst.setString(2, usuario);
        if (conex.Update(pst) > 0) {
            conex.Commit();
            resultado = true;
        }
        return resultado;
    }
    
    public boolean updateContadorDescarga(int id) throws SQLException {
        boolean resultado = false;
        String sql = "UPDATE archivo_impresion set contador = contador+1 WHERE id = ?";
        java.sql.PreparedStatement pst = conex.getConnection().prepareStatement(sql);
        pst.setInt(1, id);
        if (conex.Update(pst) > 0) {
            conex.Commit();
            resultado = true;
        }
        return resultado;
    }
    
}
