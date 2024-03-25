package br.com.darlan.gestordeprojetos.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterUtilTest {

    @Test
    void convertLocalDateToDate() {
        Date date = ConverterUtil.toDate(LocalDate.of(2022, 1, 1));
        assertEquals("2022-01-01", new SimpleDateFormat("yyy-MM-dd").format(date));
    }

    @Test
    void convertDateToLocalDate() {
        LocalDate date = ConverterUtil.toLocalDate(getDate("2022-01-01"));
        assertEquals("2022-01-01", ConverterUtil.toString(date, "yyyy-MM-dd"));
    }

    @Test
    void convertStringToLocalDate() {
        LocalDate date = ConverterUtil.toLocalDate("2022-01-01", "yyyy-MM-dd");
        assertEquals("2022-01-01", ConverterUtil.toString(date, "yyyy-MM-dd"));
    }

    @Test
    void convertLocalDateToString() {
        String date = ConverterUtil.toString(LocalDate.of(2022, 1, 1), "yyyy-MM-dd");
        assertEquals("2022-01-01", date);
    }

    private Date getDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        try {
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}