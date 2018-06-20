package com.are.docuexpress.controlador;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

public class Utilidades {

    public static String strDateServer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static String strTimeServer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static java.util.Date strToDate(String s) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date fecha = null;
        try {
            fecha = dateFormat.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fecha;
    }

    public static String DateToStr(java.util.Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(d);
    }

    public static java.util.Date strToDatetime(String s) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date fecha = null;
        try {
            fecha = dateFormat.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fecha;
    }

    public static String AhoraToString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        java.util.Date fechaActual = new java.util.Date();
        String fecha = dateFormat.format(fechaActual);
        return fecha;
    }

    public static String getTimeServer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date fechaActual = new java.util.Date();
        String fecha = dateFormat.format(fechaActual);
        return fecha;
    }

    public static String StrAddDayDateTime(java.util.Date d, int dias) {
        Calendar ahoraCal = Calendar.getInstance();
        ahoraCal.add(Calendar.DAY_OF_MONTH, dias);

        return DateToStr(ahoraCal.getTime());
    }

    public static Date DateAddDayDateTime(java.util.Date d, int dias) {
        Calendar ahoraCal = Calendar.getInstance();
        ahoraCal.add(Calendar.DAY_OF_MONTH, dias);

        return ahoraCal.getTime();
    }

    public static String tiempo(int horas) {
        String cadena = "";

        if (Math.abs(horas) < 24) {
            cadena = horas + " horas";
        } else {
            int dias = (int) horas / 24;
            int h = (horas % 24);
            cadena = dias + (dias == 1 ? " dia " : " dias ") + Math.abs(h) + " horas";
        }

        return cadena;
    }

    public static String getcontentPartText(Part input) {
        Scanner sc = null;
        String content = null;
        try {
            sc = new Scanner(input.getInputStream(), "UTF-8");
            if (sc.hasNext()) {
                content = sc.nextLine();
            } else {
                content = "";
            }
            return content;
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, ex.getMessage());
            content = null;
        } finally {
            sc.close();
        }
        return content;
    }

    public static String getContentTextArea(Part input) {
        StringBuilder sb = null;
        Scanner sc = null;
        try {
            sc = new Scanner(input.getInputStream(), "UTF-8");
            sb = new StringBuilder("");
            while (sc.hasNext()) {
                sb.append(sc.nextLine());
                sb.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, ex.getMessage());
            sb = null;
        } finally {
            sc.close();
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public static boolean guardarImagenDeProdructoEnElSistemaDeFicheros(InputStream input, String fileName)
            throws ServletException {
        FileOutputStream output = null;
        boolean ok = false;
        try {
            output = new FileOutputStream(fileName);
            int leido = 0;
            leido = input.read();
            while (leido != -1) {
                output.write(leido);
                leido = input.read();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                output.flush();
                output.close();
                input.close();
                ok = true;
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, "Error cerrando flujo de salida", ex);
            }
        }
        return ok;
    }

    public static java.util.Date SqlDateToDate(java.sql.Date d) {
        java.util.Date utilDate = new java.util.Date(d.getTime());
        return utilDate;

    }

    public static String diferenciaDates(java.sql.Timestamp inicio, java.sql.Timestamp fin) {
        long diferenciaMils = fin.getTime() - inicio.getTime();
        //obtenemos los segundos
        long segundos = diferenciaMils / 1000;

        //obtenemos las horas
        long horas = segundos / 3600;

        //restamos las horas para continuar con minutos
        segundos -= horas * 3600;

        //igual que el paso anterior
        long minutos = segundos / 60;
        segundos -= minutos * 60;

        return Long.toString(horas) + ":" + Long.toString(minutos) + ":" + Long.toString(segundos);
    }

    public static String padleft(String cadena, int pad, String str) {
        String r = "";
        for (int i = 0; i < pad - (cadena.length()); i++) {
            r += str;
        }
        return r + cadena;
    }

    public static String padright(String cadena, int pad, String str) {
        String r = "";
        for (int i = 0; i < pad - (cadena.length()); i++) {
            r += str;
        }
        return cadena + r;
    }

    public static double Distancia(double lat1, double long1, double lat2, double long2) {
        double distancia = 0;
    	//double R = 6378.137;  // radio de la tierra
        //double dLong = radianes(long2-long1);
        //double dLat = radianes(lat2-lat1);
        //Distancia = 6371 * ACOS(COS(Lat1) * COS(Lat2) * COS(Long2 - Long1) + SIN(Lat1) * SIN(Lat2)) 
        //double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(radianes(lat1)) * Math.cos(radianes(lat2)) * Math.sin(dLong/2) * Math.sin(dLong/2);
        //double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        //distancia = R * c;
        //distancia = R * Math.acos(Math.cos(radianes(lat1)) * Math.cos(radianes(lat2)) * Math.cos(radianes(long2)-radianes(long1)) + Math.sin(radianes(lat1)) * Math.sin(radianes(lat2)));

        /*double degtorad = 0.01745329;
         double radtodeg = 57.29577951;
    	
         double dlong = (long1 - long2);
    	
         double dvalue = (Math.sin(lat1 * degtorad) * Math.sin(lat2 * degtorad))
         + (Math.cos(lat1 * degtorad) * Math.cos(lat2 * degtorad)
         * Math.cos(dlong * degtorad));
         double dd = Math.acos(dvalue) * radtodeg;
         double km = (dd * 111.302);
         km = (km * 100)/100;*/
        distancia = (Math.acos(Math.sin(radianes(lat1)) * Math.sin(radianes(lat2)) + Math.cos(radianes(lat1)) * Math.cos(radianes(lat2)) * Math.cos(radianes(long1) - radianes(long2))) * 6378);

        return distancia;
    }

    public static double radianes(double x) {
        return (x * Math.PI) / 180;
    }

    public static double Redondear(double numero, int digitos) {
        int cifras = (int) Math.pow(10, digitos);
        return Math.rint(numero * cifras) / cifras;
    }

    public static String RightPad(String text, int size, char pad) {

        if (text.length() >= size) {
            return text;
        }

        int repite = size - text.length();

        for (int x = 1; x <= repite; x++) {
            text = text + pad;
        }

        return text;
    }

    public static String Espacios(int size) {
        String text = "";

        for (int x = 1; x <= size; x++) {
            text = text + " ";
        }

        return text;
    }

    public static void AgregarLog(String log, String filename) {
        FileWriter fichero = null;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            fichero = new FileWriter("C:\\Temp\\" + filename, true);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (fichero != null) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(fichero);
                java.util.Date fecha = new Date();
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                pw.append(df.format(fecha) + "\t" + log + "\r\n");
                fichero.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

}
