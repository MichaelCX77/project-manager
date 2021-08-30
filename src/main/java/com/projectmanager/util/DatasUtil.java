package com.projectmanager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

public class DatasUtil {

//	Retorna um Calendar com somando ou subtraindo a data passada e setendo a hora que for informada
//	 dia = 0 - hoje / dia = +1 - amanhã / dia = -1 - ontem
	public static Calendar alteraData(Calendar cal, Integer dia, Integer hora, Integer minuto, Integer segundo, Integer milissegundo) {
		
		if(dia != 0) {
			cal.add(Calendar.DATE,dia);
		}
		
		cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, minuto);
		cal.set(Calendar.SECOND, segundo);
		cal.set(Calendar.MILLISECOND, milissegundo);
		
		return cal;
	}
//	Retorna a Data e Hora atual como SympleDateFormat
	public static SimpleDateFormat getSimpleFormatDate(String formato) {
		
		SimpleDateFormat dataAtual = new SimpleDateFormat(formato);
		
		return dataAtual;
	}

	
//	Retorna a Data e Hora atual como Calendar
	public static Calendar getCalendarDate() {
		
		Date d = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		
		return cal;
		
	}
	
//	Retorna DIA, MES ou ANO atual como String
	public static String getData(String formato) {
		
	    Date dataTemp = new Date();
	    DateTime dataAtual = new DateTime(dataTemp); // data de hoje                		
	    String data = dataAtual.toString(formato);
		
		return data;
		
	}
	
//	Converter DateSql para SimpleFormatDate
	public static String convertAndFormatToString(Date data, String formato) {
		
		SimpleDateFormat dataAtual = new SimpleDateFormat(formato);
		
		String strDate= dataAtual.format(data);  
		
		return strDate;
	}
	
//	Converter DateSql para SimpleFormatDate
	public static Integer calcAnosPorDataDeNascimento(Integer ano, Integer mes, Integer dia) {
		
		DateTime dataAtual = new DateTime();
		DateTime dataNascimento = new DateTime(ano, mes, dia, 0, 0);
		
		Interval intervalo = new Interval(dataNascimento, dataAtual);
		Period period = intervalo.toPeriod();

		
		return period.getYears();
	}
	
	public static boolean comparaDatasDate(Date dataInicial, Date dataFinal) {

	    boolean maior = dataFinal.after(dataInicial);
	    
	    return maior;
		
	}
	
//	 Se a data final for posterior a data inicial retorna true = senão retorna false
	public static Boolean comparaDatasString(String dataInicial, String dataFinal, String formato) throws ParseException {

		SimpleDateFormat formatoString = new SimpleDateFormat(formato);
	    Date dataFinalDate = formatoString.parse(dataFinal);
	    Date dataInicialDate = formatoString.parse(dataInicial);

	    boolean maior = dataFinalDate.after(dataInicialDate);
	    
	    return maior;
		
	}
	
//	 Se a data final for posterior a data inicial retorna true = senão retorna false
	public static Boolean comparaDatasSqlDate(java.sql.Date dataInicial, java.sql.Date dataFinal, String formato) throws ParseException {

		String dataEntregaString = dataFinal.toString();
	    String dataAtualString = dataInicial.toString();
	    
	    SimpleDateFormat formatoString = new SimpleDateFormat(formato);
	    
	    Date dataFinalDate = formatoString.parse(dataEntregaString);
	    Date dataAtualDate = formatoString.parse(dataAtualString);

	    boolean maior = dataFinalDate.after(dataAtualDate);
	    
	    return maior;
	}
}
