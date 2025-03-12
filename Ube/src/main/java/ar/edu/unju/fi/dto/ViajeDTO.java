package ar.edu.unju.fi.dto;

import ar.edu.unju.fi.model.TipoViaje;
import lombok.Data;

@Data // Anotación de Lombok para generar getters, setters, toString, etc.
public class ViajeDTO {
    private Long id;
    private TipoViaje tipo;
    private double costo;
    private Long conductorId;
}