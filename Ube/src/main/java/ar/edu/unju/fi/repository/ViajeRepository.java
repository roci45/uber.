package ar.edu.unju.fi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.model.Viaje;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Viaje v WHERE v.conductor.id = :conductorId")
    void deleteByConductorId(Long conductorId);
}