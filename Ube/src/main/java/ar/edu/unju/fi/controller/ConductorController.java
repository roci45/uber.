package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ar.edu.unju.fi.dto.ConductorDTO;
import ar.edu.unju.fi.service.ConductorService;
import org.springframework.ui.Model;

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
    public String guardarConductor(@ModelAttribute("conductor") ConductorDTO conductorDTO) {
        conductorService.save(conductorDTO);
        return "redirect:/conductor";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarConductor(@PathVariable Long id) {
        conductorService.delete(id);
        return "redirect:/conductor";
    }
}