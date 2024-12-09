package com.sideralti.app.service;

import com.sideralti.app.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto crearUsuario(UsuarioDto usuarioDTO);
    UsuarioDto obtenerUsuarioPorId(Long id);
    List<UsuarioDto> obtenerTodosUsuarios();
    UsuarioDto actualizarUsuario(Long id, UsuarioDto usuarioDTO);
    void eliminarUsuario(Long id);
    List<UsuarioDto> guardarTodosUsuarios(List<UsuarioDto> usuariosDTO);
    void eliminarTodosUsuarios(List<Long> ids);
    void eliminarTodosLosUsuarios();
    Long contarTotalUsuarios();
    Long contarUsuariosActivos();
    Long contarUsuariosInactivos();
    void flushDatabaseChanges();
    void flushSpecificEntity(Long usuarioId);
    UsuarioDto crearYSincronizarUsuario(UsuarioDto usuarioDTO);
    UsuarioDto crearUsuarioUnico(UsuarioDto usuarioDTO);
    UsuarioDto actualizarUsuarioConHistorial(Long id, UsuarioDto usuarioDTO);
    UsuarioDto cambiarEstadoUsuario(Long id, boolean nuevoEstado);

    List<UsuarioDto> ordenarPorNombre(String orden);
    List<UsuarioDto> ordenarPorFechaNacimiento(String orden);
    List<UsuarioDto> ordenarPorEmail(String orden);
    List<UsuarioDto> ordenarPorActivoYNombre(String orden);
    List<UsuarioDto> ordenarPorLongitudTelefono(String orden);


    List<UsuarioDto> buscarPorNombre(String nombre);
    List<UsuarioDto> buscarPorNombreYApellido(String nombre, String apellido);
    List<UsuarioDto> buscarPorNombreYDosApellidos(String nombre, String apellido);
//    List<UsuarioDto> buscarPorEmailList(String email);
    List<UsuarioDto> buscarPorAnioNacimiento(int anio);


/**
    List<UsuarioDto> findUsersWithFlexibleSearch(
            String nombre,
            String apellido,
            String email,
            Boolean activo,
            Date startBirthDate,
            Date endBirthDate,
            String sortBy,
            String sortDirection
    );

    Sort createSort(String sortBy, String sortDirection);
    **/

}
