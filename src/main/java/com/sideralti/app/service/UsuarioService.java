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

}
