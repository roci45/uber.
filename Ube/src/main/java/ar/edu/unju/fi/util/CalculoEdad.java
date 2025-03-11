package ar.edu.unju.fi.util;

import java.time.LocalDate;
import java.time.Period;

public class CalculoEdad {

    public static int calcularEdad(LocalDate fechaNacimiento) {
        LocalDate fechaActual = LocalDate.now();
        return Period.between(fechaNacimiento, fechaActual).getYears();
    }
}