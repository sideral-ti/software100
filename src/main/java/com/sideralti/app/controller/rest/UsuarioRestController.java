package com.sideralti.app.controller.rest;

import com.sideralti.app.dto.UsuarioDto;
import com.sideralti.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Endpoint para eliminar usuarios por lista de IDs
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> eliminarTodosUsuarios(@RequestBody List<Long> ids) {
        usuarioService.eliminarTodosUsuarios(ids);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para eliminar TODOS los usuarios
    @DeleteMapping("/deleteAll/full")
    public ResponseEntity<Void> eliminarTodosLosUsuarios() {
        usuarioService.eliminarTodosLosUsuarios();
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obtener el conteo total de usuarios
    @GetMapping("/count")
    public ResponseEntity<Long> contarTotalUsuarios() {
        Long totalUsuarios = usuarioService.contarTotalUsuarios();
        return ResponseEntity.ok(totalUsuarios);
    }

    // Endpoint para obtener conteos detallados
    @GetMapping("/count/detalle")
    public ResponseEntity<Map<String, Long>> contarUsuariosDetallado() {
        Map<String, Long> conteos = new HashMap<>();
        conteos.put("total", usuarioService.contarTotalUsuarios());
        conteos.put("activos", usuarioService.contarUsuariosActivos());
        conteos.put("inactivos", usuarioService.contarUsuariosInactivos());
        return ResponseEntity.ok(conteos);
    }

    @PostMapping("/flush")
    public ResponseEntity<Void> forzarFlushBaseDeDatos() {
        usuarioService.flushDatabaseChanges();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/flush/{id}")
    public ResponseEntity<Void> forzarFlushEntidad(@PathVariable Long id) {
        usuarioService.flushSpecificEntity(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/flush/crearYSincronizarUsuario")
    public ResponseEntity<UsuarioDto> crearYSincronizarUsuario(@Valid @RequestBody UsuarioDto usuarioDTO) {
        UsuarioDto nuevoUsuario = usuarioService.crearYSincronizarUsuario(usuarioDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // Endpoint 1: Creación de usuario único
    @PostMapping("/registro-unico")
    public ResponseEntity<UsuarioDto> crearUsuarioUnico(
            @Valid @RequestBody UsuarioDto usuarioDTO) {
        UsuarioDto usuarioCreado = usuarioService.crearUsuarioUnico(usuarioDTO);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }

    // Endpoint 2: Actualización con historial
    @PutMapping("/actualizar-con-historial/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuarioConHistorial(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDto usuarioDTO) {
        UsuarioDto usuarioActualizado = usuarioService.actualizarUsuarioConHistorial(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Endpoint 3: Cambio de estado
    @PatchMapping("/cambiar-estado/{id}")
    public ResponseEntity<UsuarioDto> cambiarEstadoUsuario(
            @PathVariable Long id,
            @RequestParam boolean estado) {
        UsuarioDto usuarioActualizado = usuarioService.cambiarEstadoUsuario(id, estado);
        return ResponseEntity.ok(usuarioActualizado);
    }


    @GetMapping("/ordenar/nombre")
    public ResponseEntity<List<UsuarioDto>> ordenarPorNombre(@RequestParam(defaultValue = "asc") String orden) {
        return ResponseEntity.ok(usuarioService.ordenarPorNombre(orden));
    }

    @GetMapping("/ordenar/nombre/desc")
    public ResponseEntity<List<UsuarioDto>> ordenarPorNombreDesc(@RequestParam(defaultValue = "desc") String orden) {
        return ResponseEntity.ok(usuarioService.ordenarPorNombre(orden));
    }

    @GetMapping("/ordenar/fecha-nacimiento")
    public ResponseEntity<List<UsuarioDto>> ordenarPorFechaNacimiento(@RequestParam(defaultValue = "asc") String orden) {
        return ResponseEntity.ok(usuarioService.ordenarPorFechaNacimiento(orden));
    }

    @GetMapping("/ordenar/email")
    public ResponseEntity<List<UsuarioDto>> ordenarPorEmail(@RequestParam(defaultValue = "asc") String orden) {
        return ResponseEntity.ok(usuarioService.ordenarPorEmail(orden));
    }

    @GetMapping("/ordenar/activo-nombre")
    public ResponseEntity<List<UsuarioDto>> ordenarPorActivoYNombre(@RequestParam(defaultValue = "asc") String orden) {
        return ResponseEntity.ok(usuarioService.ordenarPorActivoYNombre(orden));
    }

    @GetMapping("/ordenar/telefono")
    public ResponseEntity<List<UsuarioDto>> ordenarPorLongitudTelefono(@RequestParam(defaultValue = "asc") String orden) {
        return ResponseEntity.ok(usuarioService.ordenarPorLongitudTelefono(orden));
    }



    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<UsuarioDto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @GetMapping("/buscar/nombre-apellido")
    public ResponseEntity<List<UsuarioDto>> buscarPorNombreYApellido(
            @RequestParam String nombre, @RequestParam String apellido) {
        return ResponseEntity.ok(usuarioService.buscarPorNombreYApellido(nombre, apellido));
    }

    @GetMapping("/buscar/nombre-dos-apellidos")
    public ResponseEntity<List<UsuarioDto>> buscarPorNombreYDosApellidos(
            @RequestParam String nombre, @RequestParam String apellidos) {
        return ResponseEntity.ok(usuarioService.buscarPorNombreYDosApellidos(nombre, apellidos));
    }

//    @GetMapping("/buscar/email")
//    public ResponseEntity<List<UsuarioDto>> buscarPorEmail(@RequestParam String email) {
//        return ResponseEntity.ok(usuarioService.buscarPorEmailList(email));
//    }

    @GetMapping("/buscar/anio-nacimiento")
    public ResponseEntity<List<UsuarioDto>> buscarPorAnioNacimiento(@RequestParam int anio) {
        return ResponseEntity.ok(usuarioService.buscarPorAnioNacimiento(anio));
    }


    /**
    // Endpoint 1: Filtrar usuarios por nombre parcial, ordenados por apellido
    @GetMapping("/por-nombre")
    public ResponseEntity<List<UsuarioDto>> buscarPorNombreParcial(
            @RequestParam(required = false) String nombre,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        List<UsuarioDto> usuarios = usuarioRepository.findUsersWithFlexibleSearch(
                nombre, null, null, null, null, null, "apellido", sortDirection
        );
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint 2: Filtrar usuarios activos por rango de fecha de nacimiento
    @GetMapping("/activos-por-edad")
    public ResponseEntity<List<UsuarioDto>> buscarActivosPorEdad(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(defaultValue = "fechaNacimiento") String sortBy
    ) {
        List<UsuarioDto> usuarios = usuarioRepository.findUsersWithFlexibleSearch(
                null, null, null, true, startDate, endDate, sortBy, "asc"
        );
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint 3: Buscar por email y ordenar por nombre
    @GetMapping("/por-email")
    public ResponseEntity<List<UsuarioDto>> buscarPorEmail(
            @RequestParam String emailParcial,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        List<UsuarioDto> usuarios = usuarioRepository.findUsersWithFlexibleSearch(
                null, null, emailParcial, null, null, null, "nombre", sortDirection
        );
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint 4: Filtrar por estado activo y rango de fechas
    @GetMapping("/estado-rango-fechas")
    public ResponseEntity<List<UsuarioDto>> buscarPorEstadoYFecha(
            @RequestParam Boolean activo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<UsuarioDto> usuarios = usuarioRepository.findUsersWithFlexibleSearch(
                null, null, null, activo, startDate, endDate, "id", "asc"
        );
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint 5: Búsqueda compleja con múltiples parámetros
    @GetMapping("/busqueda-avanzada")
    public ResponseEntity<List<UsuarioDto>> busquedaAvanzada(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "apellido") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        List<UsuarioDto> usuarios = usuarioRepository.findUsersWithFlexibleSearch(
                nombre, apellido, email, activo, null, null, sortBy, sortDirection
        );
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint 6: Ordenamiento por campos específicos
    @GetMapping("/ordenamiento")
    public ResponseEntity<List<UsuarioDto>> ordenamientoPorCampo(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        List<UsuarioDto> usuarios = usuarioRepository.findUsersWithFlexibleSearch(
                null, null, null, null, null, null, sortBy, sortDirection
        );
        return ResponseEntity.ok(usuarios);
    }
*/


}