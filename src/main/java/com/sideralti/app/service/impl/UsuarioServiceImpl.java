package com.sideralti.app.service.impl;

import com.sideralti.app.dto.UsuarioDto;
import com.sideralti.app.mapper.UsuarioMapper;
import com.sideralti.app.model.Usuario;
import com.sideralti.app.repository.UsuarioRepository;
import com.sideralti.app.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioDto crearUsuario(UsuarioDto usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toDto(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDto actualizarUsuario(Long id, UsuarioDto usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Actualizar campos
        usuarioExistente.setNombre(usuarioDTO.getNombre());
        usuarioExistente.setApellido(usuarioDTO.getApellido());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setTelefono(usuarioDTO.getTelefono());
        usuarioExistente.setDireccion(usuarioDTO.getDireccion());
        usuarioExistente.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        usuarioExistente.setActivo(usuarioDTO.getActivo());

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toDto(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public List<UsuarioDto> guardarTodosUsuarios(List<UsuarioDto> usuariosDTO) {
        // Convertir DTOs a entidades
        List<Usuario> usuarios = usuariosDTO.stream()
                .map(usuarioMapper::toEntity)
                .collect(Collectors.toList());

        // Guardar todos los usuarios
        List<Usuario> usuariosGuardados = usuarioRepository.saveAll(usuarios);

        // Convertir entidades guardadas de vuelta a DTOs
        return usuariosGuardados.stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }
}