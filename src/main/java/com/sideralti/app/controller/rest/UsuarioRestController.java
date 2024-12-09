package com.sideralti.app.controller.rest;

import com.sideralti.app.dto.UsuarioDto;
import com.sideralti.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/usuario")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> crearUsuario(@Valid @RequestBody UsuarioDto usuarioDTO) {
        UsuarioDto nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerUsuario(@PathVariable Long id) {
        UsuarioDto usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDto usuarioDTO) {
        UsuarioDto usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/saveAll")
    public ResponseEntity<List<UsuarioDto>> guardarTodosUsuarios(
            @Valid @RequestBody List<UsuarioDto> usuariosDTO) {
        List<UsuarioDto> usuariosGuardados = usuarioService.guardarTodosUsuarios(usuariosDTO);
        return new ResponseEntity<>(usuariosGuardados, HttpStatus.CREATED);
    }

    @PostMapping("/saveAll2")
    public ResponseEntity<List<UsuarioDto>> guardarTodosUsuarios2(
            @Valid @RequestBody List<@Valid UsuarioDto> usuariosDTO) {
        List<UsuarioDto> usuariosGuardados = usuarioService.guardarTodosUsuarios(usuariosDTO);
        return new ResponseEntity<>(usuariosGuardados, HttpStatus.CREATED);
    }


}