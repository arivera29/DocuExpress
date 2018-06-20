package com.are.docuexpress.controlador;

import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.NamingException;

public class ControladorSuscripcion {
	
	private String fecha = "";
	long dias = 0;
	
	
	
	public long getDias() {
		return dias;
	}


	public String getFecha() {
		return fecha;
	}


	public boolean verify() {
		boolean result = false;
		String licencia = "";
		javax.naming.Context ctx;
		try {
			ctx = new javax.naming.InitialContext();
			javax.naming.Context env = (Context) ctx.lookup("java:comp/env");
			licencia = (String) env.lookup("controller");
			fecha = (String) env.lookup("service");
			System.out.println("Fecha " + fecha + " Licencia: " + licencia);
			if (licencia.equals("0")) {
				Date limite = Utilidades.strToDate(fecha);
				Date hoy = new Date();
				this.dias = this.DiffDias(hoy, limite);
				
				//System.out.println("Fecha limite " + limite.toString() + " Hoy: " + hoy.toString());
				if (hoy.before(limite)) {
					result = true;
				}
			
			}else {
				result = true;
			}
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	private long DiffDias(Date f1, Date f2) {

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.setTime(f1);
		cal2.setTime(f2);
		
		long milis1 = cal1.getTimeInMillis();
		long milis2 = cal2.getTimeInMillis();
		
		// calcular la diferencia en milisengundos
		long diff = milis2 - milis1;

		// calcular la diferencia en dias
		long diffDays = diff / (24 * 60 * 60 * 1000);

		
		return diffDays;
	}
}
