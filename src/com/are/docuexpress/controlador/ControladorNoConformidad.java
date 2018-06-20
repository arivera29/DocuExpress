/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.are.docuexpress.entidad.NoConformidad;
import com.are.docuexpress.entidad.TipoNoConformidad;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Aimer
 */
public class ControladorNoConformidad {

    private db conexion;
    private NoConformidad nc;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public NoConformidad getNc() {
        return nc;
    }

    public void setNc(NoConformidad nc) {
        this.nc = nc;
    }

    public ControladorNoConformidad(db conexion) {
        this.conexion = conexion;
    }

    public ControladorNoConformidad(db conexion, NoConformidad nc) {
        this.conexion = conexion;
        this.nc = nc;
    }

    public boolean add(NoConformidad nc) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO NoConformidad (ncIdArchivo,ncTipoNc,ncObservacion,ncEstado,ncUsuario,ncFecha,ncObservacionCierre,ncUsuarioCierre,ncFechaCierre,ncIdDocumento,ncTipo)"
                + " VALUES (?,?,?,?,?,SYSDATETIME(),null,'',null,?,?) ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, nc.getIdArchivo());
        pst.setString(2, nc.getTipoNoConformidad());
        pst.setString(3, nc.getObservacion());
        pst.setInt(4, nc.getEstado());
        pst.setString(5, nc.getUsuario());
        pst.setInt(6, nc.getIdDocumento());
        pst.setInt(7, nc.getTipo());

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            this.EnviarNotificacionCorreo(nc);
            result = true;
        }

        return result;
    }
    
    public int CantidadNoConformidad(int id, int tipo) throws SQLException {
        int result=0;
        String sql = "SELECT count(*) cantidad FROM NoConformidad WHERE ncIdArchivo=?";
        if (tipo == 2) {
            sql = "SELECT count(*) cantidad FROM NoConformidad WHERE ncIdDocumento=?";
        }
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, id);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            result = rs.getInt("cantidad");
        }
        rs.close();
        
        return result;
    }

    public boolean EnviarNotificacionCorreo(NoConformidad nc) throws SQLException {

        String tipoNc = nc.getTipoNoConformidad();
        ControladorTipoNoConformidad controlador = new ControladorTipoNoConformidad(conexion);
        if (!controlador.find(tipoNc)) {
            return false;
        }
        TipoNoConformidad tipo = controlador.getTipo();
        String correo1 = tipo.getCorreoNotificacion1();
        String correo2 = tipo.getCorreoNotificacion2();
        
        if (correo1.equals("") && correo2.equals("")) {
            return false;
        }
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        String Username = "aimer.rivera@are-soluciones.com";
        String PassWord = "Pantera2341";
        
        String body = "<html>";
        body += "<header></header>";
        body += "<body>";
        body += "<h2>Notificacion de No Conformidad - HGC</h2>";
        body += "<hr>";
        body += "<p>Se ha generado una nueva no conformidad de tipo <strong>" + tipo.getNombre() + "</strong><br>";
        body += "Para mayor información consultar <a href=\"http://www.hgc.com.co:8080/app\">http://www.hgc.com.co:8080/app</a> <br>";
        body += "<strong>Usuario registro NC: </strong>" + nc.getUsuario() + "<br>";
        body += "<strong>Observacion NC: </strong>" + nc.getObservacion() + "<br>";
        body += "<strong>Id de archivo: </strong>" + nc.getIdArchivo() + "<br>";
        body += "</p>";
        body += "<br>";
        body += "<br>";
        body += "<hr>";
        body += "<p>Este es un mensaje automático, favor no contestar <br>";
        body += "<p>HGC - Herramienta de Gestión de correspondencia - Todos los derechos reservados<br>";
        body += "<p>Soluciones Integrales ARE - <a href=\"http://www.are-soluciones.com\">www.are-soluciones.com</a>";
        body += "</body>";
        body += "</html>";

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Username, PassWord);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Username));
            if (!correo1.equals("") && !correo2.equals("")) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo1));
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(correo2));
            }else if (!correo1.equals("") && correo2.equals("")) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo1));
            }else if (correo1.equals("") && !correo2.equals("")) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo2));
            }
            
            message.setSubject("Notificacion no conformidad");
            message.setContent(body,"text/html");

            Transport.send(message);

            return true;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }



    }

}
