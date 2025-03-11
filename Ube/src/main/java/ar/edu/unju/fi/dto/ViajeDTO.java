package ar.edu.unju.fi.dto;


import ar.edu.unju.fi.model.TipoViaje;
import lombok.Data;

@Data
public class ViajeDTO {
    private Long id;
    private TipoViaje tipo;
    private double costo;
    private Long conductorId;
}