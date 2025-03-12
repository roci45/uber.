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

    public ViajeDTO crearViaje(ViajeDTO viajeDTO) {
        // Busca el conductor por su ID
        Conductor conductor = conductorRepository.findById(viajeDTO.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

        // Verifica si el conductor está disponible
        if (!conductor.isDisponible()) {
            throw new RuntimeException("El conductor no está disponible.");
        }

        // Marca al conductor como no disponible
        marcarComoNoDisponible(conductor.getId());

        // Calcula el costo del viaje
        double costoBase = calcularCostoBase(viajeDTO.getTipo());
        double costoFinal = costoBase * (1 + conductor.getTipoAutomovil().getTarifaAdicional());
        viajeDTO.setCosto(costoFinal);

        // Crea el viaje
        Viaje viaje = convertToEntity(viajeDTO);
        viaje.setConductor(conductor);
        viajeRepository.save(viaje);

        return viajeDTO;
    }

    public void cancelarViaje(Long viajeId) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        // Marcar al conductor como disponible
        marcarComoDisponible(viaje.getConductor().getId());

        // Marcar el viaje como cancelado
        viaje.setEstado(false);
        viajeRepository.save(viaje);
    }


    public List<ViajeDTO> findAll() {
        return viajeRepository.findAll().stream()
                .filter(Viaje::isEstado) // Solo viajes activos
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Viaje convertToEntity(ViajeDTO dto) {
        Viaje viaje = new Viaje();
        viaje.setId(dto.getId());
        viaje.setTipo(dto.getTipo());
        viaje.setCosto(dto.getCosto());
        viaje.setEstado(true); // Asegúrate de que el viaje esté activo

        Conductor conductor = conductorRepository.findById(dto.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        viaje.setConductor(conductor);

        return viaje;
    }

    private ViajeDTO convertToDTO(Viaje viaje) {
        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setTipo(viaje.getTipo());
        dto.setCosto(viaje.getCosto());
        dto.setConductorId(viaje.getConductor().getId());
        return dto;
    }

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
    


private void marcarComoNoDisponible(Long conductorId) {
    Conductor conductor = conductorRepository.findById(conductorId)
            .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
    conductor.setDisponible(false); // Marcar al conductor como no disponible
    conductorRepository.save(conductor);
}

    
    private void marcarComoDisponible(Long conductorId) {
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        conductor.setDisponible(true); // Marcar al conductor como disponible
        conductorRepository.save(conductor);
    }


    public void delete(Long id) {
        viajeRepository.deleteById(id); // Elimina el viaje por su ID
    }

    public List<Conductor> findConductoresDisponibles() {
        return conductorRepository.findByDisponibleTrue(); // Solo conductores disponibles
    }
}