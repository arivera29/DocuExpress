/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.docuexpress.controlador;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Aimer
 */
public class ControladorGuia {

    private db conexion;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public ControladorGuia(db conexion) {
        this.conexion = conexion;
    }

    public boolean Find(String numeroGuia) throws SQLException {

        String sql = "SELECT numeroGuia FROM guias WHERE numeroGuia=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, numeroGuia);
        java.sql.ResultSet rs = conexion.Query(pst);
        return rs.next();
    }

    public boolean UploadImgen(com.are.docuexpress.entidad.JsonImagenGuia guia, String path) throws IOException, DocumentException, SQLException {
        if (guia != null) {
            String numeroGuia = guia.getNumeroGuia();
            String contenido = guia.getJsonImagen();

            String name = Utilidades.AhoraToString() + "_GUIA_" + guia.getNumeroGuia() + ".jpg";
            String fnameImage = path + File.separator +  name;
            String fnamePDF = fnameImage + ".pdf";

            FileOutputStream fos = new FileOutputStream(fnameImage);
            byte[] bytes = Base64.decode(contenido);
            fos.write(bytes);
            fos.close();

            createPdf(fnamePDF, fnameImage);

            File f1 = new File(fnameImage);
            File f2 = new File(fnamePDF);

            if (f2.exists() && f2.length() > 0) {
                String sql = "UPDATE guias SET uploadImagen=1, fechaUploadImagen=SYSDATETIME(), usuarioUploadImagen=?, filename=?"
                        + " WHERE numeroGuia=?";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, guia.getUsuarioCarga());
                pst.setString(2, fnamePDF);
                pst.setString(3, guia.getNumeroGuia());
                if (conexion.Update(pst) > 0) {
                    f1.delete();  // Se borra el archivo de Imagen
                    return true;
                }else {
                    f2.delete();  // se borra archivo PDF
                }
                
            }

            f1.delete();  // Se borra el archivo de Imagen

        }
        return false;
    }

    public void createPdf(String filename, String fileFoto) throws IOException, DocumentException {
        Document document = new Document(PageSize.LETTER, 36, 36, 54, 36);
        Paragraph parrafo;
        Image imagen = Image.getInstance(fileFoto);
        imagen.scaleToFit(400, 400); // Scale

        PdfWriter.getInstance(document, new FileOutputStream(filename));

        document.open();
        //Creamos una cantidad significativa de paginas para probar el encabezado
        //parrafo = new Paragraph("FOTO GUIA");
        //parrafo.setAlignment(Element.ALIGN_CENTER);

        imagen.setAlignment(Element.ALIGN_CENTER);

        //document.add(parrafo);
        document.add(imagen);

        document.close();
    }

}
