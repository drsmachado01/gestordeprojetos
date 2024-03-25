package br.com.darlan.gestordeprojetos.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConverterUtil {

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static Date toDate(LocalDate dateAsLocalDate) {
		return Date.from(dateAsLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(String dateAsString, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(dateAsString, formatter);
	}

	public static LocalDate toLocalDate(Date dateAsDate) {
		return Instant.ofEpochMilli(dateAsDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static String toString(LocalDate dateAsLocalDate, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dateAsLocalDate.format(formatter);
	}
}
