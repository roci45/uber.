package ar.edu.unju.fi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.ViajeDTO;
import ar.edu.unju.fi.model.Conductor;
import ar.edu.unju.fi.model.TipoViaje;
import ar.edu.unju.fi.model.Viaje;
import ar.edu.unju.fi.repository.ConductorRepository;
import ar.edu.unju.fi.repository.ViajeRepository;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    public ViajeDTO reservarViaje(ViajeDTO viajeDTO) {
        
        Conductor conductor = conductorRepository.findById(viajeDTO.getConductorId())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        
        double costoBase = calcularCostoBase(viajeDTO.getTipo());
        double costoFinal = costoBase * (1 + conductor.getTipoAutomovil().getTarifaAdicional());
        viajeDTO.setCosto(costoFinal);
        
        Viaje viaje = convertToEntity(viajeDTO);
        viaje.setConductor(conductor);

        viajeRepository.save(viaje);

        return convertToDTO(viaje);
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

    // Método para convertir de ViajeDTO a Viaje
    private Viaje convertToEntity(ViajeDTO dto) {
        Viaje viaje = new Viaje();
        viaje.setId(dto.getId());
        viaje.setTipo(dto.getTipo());
        viaje.setCosto(dto.getCosto());
        return viaje;
    }

    // Método para convertir de Viaje a ViajeDTO
    private ViajeDTO convertToDTO(Viaje viaje) {
        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setTipo(viaje.getTipo());
        dto.setCosto(viaje.getCosto());
        dto.setConductorId(viaje.getConductor().getId());
        return dto;
    }

}