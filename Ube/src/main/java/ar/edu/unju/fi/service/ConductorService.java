package ar.edu.unju.fi.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.ConductorDTO;
import ar.edu.unju.fi.model.Conductor;
import ar.edu.unju.fi.repository.ConductorRepository;
import ar.edu.unju.fi.repository.ViajeRepository;
@Service
public class ConductorService {

    @Autowired
    private ConductorRepository conductorRepository;

    public List<ConductorDTO> findAllActive() {
        return conductorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    
    public ConductorDTO save(ConductorDTO conductorDTO) {
        // Validar la edad del conductor
        LocalDate fechaNacimiento = conductorDTO.getFechaNacimiento();
        LocalDate fechaActual = LocalDate.now();
        int edad = Period.between(fechaNacimiento, fechaActual).getYears();

        if (edad < 19) {
            throw new IllegalArgumentException("El conductor debe tener al menos 19 años.");
        }

        // Guardar el conductor si pasa la validación
        Conductor conductor = convertToEntity(conductorDTO);
        return convertToDTO(conductorRepository.save(conductor));
    }
    @Autowired
    private ViajeRepository viajeRepository; // Repositorio de Viaje

    public void delete(Long id) {
        // Eliminar todos los viajes asociados al conductor
        viajeRepository.deleteById(id);

        // Eliminar el conductor
        conductorRepository.deleteById(id);
    }

    private ConductorDTO convertToDTO(Conductor conductor) {
    	
        ConductorDTO dto = new ConductorDTO();
        dto.setId(conductor.getId());
        dto.setNombre(conductor.getNombre());
        dto.setFechaNacimiento(conductor.getFechaNacimiento());
        dto.setTipoAutomovil(conductor.getTipoAutomovil());
        dto.setActivo(conductor.isActivo());
        return dto;
    }

    private Conductor convertToEntity(ConductorDTO dto) {
    
        Conductor conductor = new Conductor();
        conductor.setId(dto.getId());
        conductor.setNombre(dto.getNombre());
        conductor.setFechaNacimiento(dto.getFechaNacimiento());
        conductor.setTipoAutomovil(dto.getTipoAutomovil());
        conductor.setActivo(dto.isActivo());
        return conductor;
    }
    
    public void marcarComoNoDisponible(Long conductorId) {
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        conductor.setDisponible(false);
        conductorRepository.save(conductor);
    }
    
    public void marcarComoDisponible(Long conductorId) {
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        conductor.setDisponible(true);
        conductorRepository.save(conductor);
    }
    
    public List<ConductorDTO> findConductoresDisponibles() {
        return conductorRepository.findByDisponibleTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
}