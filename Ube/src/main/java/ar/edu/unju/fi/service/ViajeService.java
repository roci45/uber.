package ar.edu.unju.fi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.ViajeDTO;
import ar.edu.unju.fi.model.Conductor;
import ar.edu.unju.fi.model.TipoViaje;
import ar.edu.unju.fi.model.Viaje;
import ar.edu.unju.fi.repository.ConductorRepository;
import ar.edu.unju.fi.repository.ViajeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViajeService {


    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ConductorRepository conductorRepository;
    
    public void cancelarViaje(Long viajeId) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        // Marcar al conductor como disponible
        marcarComoDisponible(viaje.getConductor().getId());

        // Eliminar el viaje
        viajeRepository.delete(viaje);
    }

    public void marcarComoNoDisponible1(Long conductorId) {
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
    
    public List<Conductor> obtenerConductoresDisponibles() {
        return conductorRepository.findByActivoTrue(); // Solo conductores disponibles
    }
    // Método para crear un viaje
    public ViajeDTO crearViaje(ViajeDTO viajeDTO) {
        Conductor conductor = conductorRepository.findById(viajeDTO.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

        if (!conductor.isDisponible()) {
            throw new IllegalArgumentException("El conductor no está disponible.");
        }

        // Marcar al conductor como no disponible
        marcarComoNoDisponible1(conductor.getId());

        // Calcular el costo del viaje
        double costoBase = calcularCostoBase(viajeDTO.getTipo());
        double costoFinal = costoBase * (1 + conductor.getTipoAutomovil().getTarifaAdicional());
        viajeDTO.setCosto(costoFinal); // Asegúrate de que el campo se llame "costo"

        // Crear el viaje
        Viaje viaje = convertToEntity(viajeDTO);
        viaje.setConductor(conductor); // Asignar el conductor al viaje
        viajeRepository.save(viaje);

        return convertToDTO(viaje);
    }

	// Método para convertir de ViajeDTO a Viaje
    private Viaje convertToEntity(ViajeDTO dto) {
        Viaje viaje = new Viaje();
        viaje.setId(dto.getId());
        viaje.setTipo(dto.getTipo());
        viaje.setCosto(dto.getCosto());

        // Asignar el conductor al viaje
        Conductor conductor = conductorRepository.findById(dto.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        viaje.setConductor(conductor);

        return viaje;
    }

    // Método para convertir de Viaje a ViajeDTO
    private ViajeDTO convertToDTO(Viaje viaje) {
        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setTipo(viaje.getTipo());
        dto.setCosto(viaje.getCosto()); // Asegúrate de que el campo se llame "costo"
        dto.setConductorId(viaje.getConductor().getId());
        return dto;
    }


    
    // Método para obtener todos los viajes
    public List<ViajeDTO> findAll() {
        return viajeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para guardar un viaje
    public void save(ViajeDTO viajeDTO) {
        Conductor conductor = conductorRepository.findById(viajeDTO.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

        double costoBase = calcularCostoBase(viajeDTO.getTipo());
        double costoFinal = costoBase * (1 + conductor.getTipoAutomovil().getTarifaAdicional());
        viajeDTO.setCosto(costoFinal);

        Viaje viaje = convertToEntity(viajeDTO);
        viaje.setConductor(conductor);

        viajeRepository.save(viaje);
    }

    // Método para eliminar un viaje por su ID
    public void delete(Long id) {
        viajeRepository.deleteById(id);
    }

    // Método para calcular el costo base según el tipo de viaje
    private double calcularCostoBase(TipoViaje tipoViaje) {
        switch (tipoViaje) {
            case CORTA:
                return 7000;
            case MEDIA:
                return 10000;
            case LARGA:
                return 20000;
            default:
                throw new IllegalArgumentException("Tipo de viaje no válido");
        }
    }
}