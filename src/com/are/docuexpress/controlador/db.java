/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

/**
 *
 * @author Aimer
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.NamingException;

public class db {

    private Connection conn = null;
    private int FilasAfectadas = 0;
    //private String connectionUrl  = "jdbc:jtds:sqlserver://localhost:1433;databaseName=liquidador;integratedSecurity=true";
    private String connectionUrl = "jdbc:jtds:sqlserver://localhost:1433/docuexpress;autocommit=false";

    public db() throws SQLException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl, "docuexpress", "pantera");
            this.Query("SET ANSI_WARNINGS OFF;");

            if (conn == null) {
                throw new SQLException("Problema de conexion con el Servidor");
            } else {
                conn.setAutoCommit(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return conn;
    }

    public ResultSet Query(String sql) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        return rs;
    }

    public ResultSet Query(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.executeQuery();
        return rs;
    }

    public int Update(String sql) throws SQLException {
        Statement st = conn.createStatement();
        FilasAfectadas = st.executeUpdate(sql);
        return FilasAfectadas;
    }

    public int Update(PreparedStatement pst) throws SQLException {
        FilasAfectadas = pst.executeUpdate();
        return FilasAfectadas;
    }

    public int Update(String sql, int Modo) throws SQLException {
        Statement st = conn.createStatement();
        FilasAfectadas = st.executeUpdate(sql);
        if (Modo == 1) {  // efectuar el Commit
            this.Commit();
        }
        return FilasAfectadas;
    }

    public void Close() throws SQLException {
        conn.close();
    }
    
    public void BeginTransacction() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("BEGIN TRANSACTION;");
        
    }
    
    public void CommitTransacction() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("COMMIT;");
    }
    
    public void RollbackTransacction() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("ROLLBACK;");
    }

    public void Commit() throws SQLException {
        conn.commit();
    }

    public void Rollback() throws SQLException {
        conn.rollback();
    }

    public int getLastId() throws SQLException {
        int last_id = -1;
        String sql = "SELECT LAST_INSERT_ID()";
        ResultSet rs = this.Query(sql);
        if (rs.next()) {
            last_id = rs.getInt(1);
        }
        rs.close();
        return last_id;
    }

    public String current_date() throws SQLException {
        String sql = "select SYSDATETIME() as today;";
        java.sql.ResultSet rs = this.Query(sql);
        if (rs.next()) {
            return rs.getString("today");
        } else {
            return "";
        }

    }

    public ArrayList resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList results = new ArrayList();

        while (rs.next()) {
            HashMap row = new HashMap();
            results.add(row);

            for (int i = 1; i <= columns; i++) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
        }
        return results;
    }

    public void ReadConfigDatabase() {
        javax.naming.Context ctx;
        try {
            ctx = new javax.naming.InitialContext();
            javax.naming.Context env = (Context) ctx.lookup("java:comp/env");
            this.connectionUrl = (String) env.lookup("url");
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    	// obtain the greetings message 
        //configured by the deployer
    }

}
