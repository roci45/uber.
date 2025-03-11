package ar.edu.unju.fi.dto;

import java.time.LocalDate;

import ar.edu.unju.fi.model.TipoAutomovil;
import lombok.Data;

@Data
public class ConductorDTO {
    private Long id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private TipoAutomovil tipoAutomovil;
    private boolean activo;
}