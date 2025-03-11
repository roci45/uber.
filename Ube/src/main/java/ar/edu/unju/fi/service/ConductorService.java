package ar.edu.unju.fi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.ConductorDTO;
import ar.edu.unju.fi.model.Conductor;
import ar.edu.unju.fi.repository.ConductorRepository;
@Service
public class ConductorService {

    @Autowired
    private ConductorRepository conductorRepository;

    public List<ConductorDTO> findAllActive() {
        return conductorRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConductorDTO save(ConductorDTO conductorDTO) {
        Conductor conductor = convertToEntity(conductorDTO);
        return convertToDTO(conductorRepository.save(conductor));
    }

    public void delete(Long id) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));
        conductor.setActivo(false); // Cambia el estado a inactivo
        conductorRepository.save(conductor);
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
}