package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ar.edu.unju.fi.dto.ConductorDTO;
import ar.edu.unju.fi.service.ConductorService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/conductor")
public class ConductorController {

    @Autowired
    private ConductorService conductorService;

    @GetMapping
    public String listarConductores(Model model) {
        model.addAttribute("conductores", conductorService.findAllActive());
        return "listaconductor";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("conductor", new ConductorDTO());
        return "conductornuevo";
    }


    @PostMapping("/guardar")
    public String guardarConductor(@Valid @ModelAttribute("conductor") ConductorDTO conductorDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario
            return "conductornuevo";
        }

        try {
            conductorService.save(conductorDTO);
            return "redirect:/conductor"; // Redirigir a la lista de conductores después de guardar
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // Mensaje de error
            model.addAttribute("conductor", conductorDTO); // Mantener los datos del formulario
            return "conductornuevo"; // Volver al formulario con el mensaje de error
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Errores de validación:<br>");

        for (FieldError error : result.getFieldErrors()) {
            errorMessage.append("- ").append(error.getDefaultMessage()).append("<br>");
        }

        model.addAttribute("error", errorMessage.toString());
        return "conductornuevo"; // Volver al formulario con los mensajes de error
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarConductor(@PathVariable Long id) {
        conductorService.delete(id); // Eliminar el conductor
        return "redirect:/conductor"; // Redirigir a la lista de conductores después de eliminar
    }
}