package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ar.edu.unju.fi.dto.ViajeDTO;
import ar.edu.unju.fi.service.ViajeService;

@Controller
@RequestMapping("/viaje")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    // Método para listar todos los viajes
    @GetMapping
    public String listarViajes(Model model) {
        model.addAttribute("viajes", viajeService.findAll());
        return "listaviaje"; // Nombre de la vista para listar viajes
    }

    // Método para mostrar el formulario de creación de un nuevo viaje
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("viaje", new ViajeDTO());
        model.addAttribute("conductores", viajeService.obtenerConductores()); // Obtener lista de conductores
        return "nuevoviaje"; // Nombre de la vista para crear un nuevo viaje
    }

    // Método para guardar un nuevo viaje
    @PostMapping("/guardar")
    public String guardarViaje(@ModelAttribute("viaje") ViajeDTO viajeDTO) {
        viajeService.save(viajeDTO);
        return "redirect:/viaje"; // Redirigir a la lista de viajes después de guardar
    }

    // Método para eliminar un viaje
    @GetMapping("/eliminar/{id}")
    public String eliminarViaje(@PathVariable Long id) {
        viajeService.delete(id);
        return "redirect:/viaje"; // Redirigir a la lista de viajes después de eliminar
    }

    // Método para reservar un viaje (opcional, si lo necesitas)
    @GetMapping("/reservar/{conductorId}")
    public String mostrarFormularioReserva(@PathVariable Long conductorId, Model model) {
        model.addAttribute("viaje", new ViajeDTO());
        model.addAttribute("conductorId", conductorId);
        return "reservaviaje"; // Nombre de la vista para reservar un viaje
    }

    @PostMapping("/reservar")
    public String reservarViaje(@ModelAttribute("viaje") ViajeDTO viajeDTO) {
        viajeService.reservarViaje(viajeDTO);
        return "redirect:/conductores"; // Redirigir a la lista de conductores después de reservar
    }
}