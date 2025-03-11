package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ar.edu.unju.fi.dto.ViajeDTO;
import ar.edu.unju.fi.service.ViajeService;

@Controller
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @GetMapping("/reservar/{conductorId}")
    public String mostrarFormularioReserva(@PathVariable Long conductorId, Model model) {
        model.addAttribute("viaje", new ViajeDTO());
        model.addAttribute("conductorId", conductorId);
        return "viajes/reserva";
    }

    @PostMapping("/reservar")
    public String reservarViaje(@ModelAttribute("viaje") ViajeDTO viajeDTO) {
        viajeService.reservarViaje(viajeDTO);
        return "redirect:/conductores";
    }
}