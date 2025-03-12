package ar.edu.unju.fi.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "conductores")
public class Conductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "fecha_nacimiento", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")   
    @Past (message="La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_automovil", nullable = false)
    private TipoAutomovil tipoAutomovil;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "disponible", nullable = false)
    private boolean disponible = true; // Nuevo campo para rastrear disponibilidad

    @OneToMany(mappedBy = "conductor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Viaje> viajes; // Relación con la entidad Viaje
}


