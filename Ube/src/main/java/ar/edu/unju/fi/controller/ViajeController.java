package ar.edu.unju.fi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.unju.fi.dto.ViajeDTO;
import ar.edu.unju.fi.service.ConductorService;
import ar.edu.unju.fi.service.ViajeService;
@Controller
@RequestMapping("/viaje")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @Autowired
    private ConductorService conductorService;

    // Método para listar todos los viajes
    @GetMapping
    public String listarViajes(Model model) {
        model.addAttribute("viajes", viajeService.findAll()); // Obtener todos los viajes activos
        return "listaviaje"; // Nombre de la vista para listar viajes
    }

    // Método para mostrar el formulario de creación de un nuevo viaje
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("viajeDTO", new ViajeDTO()); 
        model.addAttribute("conductores", conductorService.findConductoresDisponibles()); // Lista de conductores
        model.addAttribute("tipos", List.of("Corta", "Media", "Larga")); // Lista de tipos de viaje
        return "nuevoviaje";
    }

    // Método para guardar un nuevo viaje
    @PostMapping("/guardar")
    public String guardarViaje(@ModelAttribute ViajeDTO viajeDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Llama al servicio para crear el viaje
            ViajeDTO viajeCreado = viajeService.crearViaje(viajeDTO);

    
            redirectAttributes.addFlashAttribute("mensaje", "Viaje creado exitosamente con un costo de: " + viajeCreado.getCosto());

            // Redirige a la lista de viajes
            return "redirect:/viaje";
        } catch (IllegalArgumentException e) {
           
            model.addAttribute("error", e.getMessage());

            // Vuelve a cargar los datos necesarios para el formulario
            model.addAttribute("tipos", List.of("Corta", "Media", "Larga")); // Lista de tipos de viaje
            model.addAttribute("conductores", conductorService.findConductoresDisponibles()); // Lista de conductores disponibles

            // Vuelve al formulario de creación de viaje
            return "nuevoviaje";
        }
    }
    // Método para eliminar un viaje
    @GetMapping("/eliminar/{id}")
    public String eliminarViaje(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            viajeService.delete(id); // Eliminar el viaje
            redirectAttributes.addFlashAttribute("mensaje", "Viaje eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Mensaje de error
        }
        return "redirect:/viaje"; // Redirigir a la lista de viajes después de eliminar
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarViaje(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            viajeService.cancelarViaje(id); // Llama al método cancelarViaje del servicio
            redirectAttributes.addFlashAttribute("mensaje", "Viaje cancelado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Mensaje de error
        }
        return "redirect:/viaje"; // Redirige a la lista de viajes
    }

    // Método para reservar un viaje (similar a guardar, pero con redirección a conductores)
    @PostMapping("/reservar")
    public String reservarViaje(@ModelAttribute("viajeDTO") ViajeDTO viajeDTO, RedirectAttributes redirectAttributes) {
        try {
            ViajeDTO viajeCreado = viajeService.crearViaje(viajeDTO); 
            redirectAttributes.addFlashAttribute("mensaje", "Viaje reservado exitosamente con un costo de: " + viajeCreado.getCosto());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Mensaje de error
        }
        return "redirect:/conductores"; // Redirigir a la lista de conductores después de reservar
    }
}