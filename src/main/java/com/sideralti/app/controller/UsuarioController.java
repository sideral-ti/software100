package com.sideralti.app.controller;

import com.sideralti.app.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public String guardarNuevoUsuario( Model model) {
        model.addAttribute("usuario", usuarioService.crearUsuario(null));
        return "usuario";
    }

}
