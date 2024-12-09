package com.sideralti.app.service.impl;

import com.sideralti.app.dto.UsuarioDto;
import com.sideralti.app.mapper.UsuarioMapper;
import com.sideralti.app.model.Usuario;
import com.sideralti.app.repository.UsuarioRepository;
import com.sideralti.app.service.UsuarioService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EntityManager entityManager;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, EntityManager entityManager) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.entityManager = entityManager;
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

    @Transactional
    public void eliminarTodosUsuarios(List<Long> ids) {
        // Verificar que los usuarios existan
        List<Usuario> usuariosAEliminar = usuarioRepository.findAllById(ids);

        if (usuariosAEliminar.size() != ids.size()) {
            // Si no se encuentran todos los IDs, lanzar excepción
            throw new EntityNotFoundException("Uno o más usuarios no encontrados");
        }

        // Eliminar todos los usuarios
        usuarioRepository.deleteAllById(ids);
    }

    // Método para eliminar todos los usuarios
    @Transactional
    public void eliminarTodosLosUsuarios() {
        usuarioRepository.deleteAll();
    }

    // Método para contar todos los usuarios
    @Transactional(readOnly = true)
    public Long contarTotalUsuarios() {
        return usuarioRepository.count();
    }

    // Método para contar usuarios activos
    @Transactional(readOnly = true)
    public Long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    // Método para contar usuarios inactivos
    @Transactional(readOnly = true)
    public Long contarUsuariosInactivos() {
        return usuarioRepository.countByActivoFalse();
    }

    @Transactional
    public void flushDatabaseChanges() {
        // Forzar la sincronización de todas las operaciones pendientes con la base de datos
        usuarioRepository.flush();
    }

    @Transactional
    public void flushSpecificEntity(Long usuarioId) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Realizar alguna modificación
        usuario.setNombre(usuario.getNombre() + " (Actualizado)");
        usuarioRepository.save(usuario);

        // Forzar flush para este usuario específico
        entityManager.flush();
    }

    @Transactional
    public UsuarioDto crearYSincronizarUsuario(UsuarioDto usuarioDTO) {
        // Convertir DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        // Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Forzar la sincronización inmediata con la base de datos
        usuarioRepository.flush();

        // Devolver DTO actualizado
        return usuarioMapper.toDto(usuarioGuardado);
    }


    // Endpoint 1: Creación de usuario con validación única de email
    @Transactional
    public UsuarioDto crearUsuarioUnico(UsuarioDto usuarioDTO) {
        // Verificar si el email ya existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.getEmail());

        if (usuarioExistente.isPresent()) {
            throw new EntityExistsException("El email ya está registrado");
        }

        // Convertir DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        // Establecer fecha de creación
        usuario.setFechaNacimiento(new Date());

        // Guardar y forzar sincronización
        Usuario usuarioGuardado = usuarioRepository.saveAndFlush(usuario);

        // Convertir y devolver DTO
        return usuarioMapper.toDto(usuarioGuardado);
    }

    // Endpoint 2: Actualización con validación y registro histórico
    @Transactional
    public UsuarioDto actualizarUsuarioConHistorial(Long id, UsuarioDto usuarioDTO) {
        // Buscar usuario existente
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar cambios significativos
        boolean cambiosSignificativos = !usuarioExistente.getNombre().equals(usuarioDTO.getNombre()) ||
                !usuarioExistente.getEmail().equals(usuarioDTO.getEmail());

        // Actualizar campos
        usuarioExistente.setNombre(usuarioDTO.getNombre());
        usuarioExistente.setApellido(usuarioDTO.getApellido());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setTelefono(usuarioDTO.getTelefono());
        usuarioExistente.setDireccion(usuarioDTO.getDireccion());

        // Si hay cambios significativos, marcar con una bandera
        if (cambiosSignificativos) {
            usuarioExistente.setActivo(false); // Requiere revisión
        }

        // Guardar y forzar sincronización
        Usuario usuarioActualizado = usuarioRepository.saveAndFlush(usuarioExistente);

        return usuarioMapper.toDto(usuarioActualizado);
    }

    // Endpoint 3: Activación/Desactivación con validaciones
    @Transactional
    public UsuarioDto cambiarEstadoUsuario(Long id, boolean nuevoEstado) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar transición de estado
        if (usuario.getActivo() == nuevoEstado) {
            throw new IllegalStateException("El usuario ya está en ese estado");
        }

        // Cambiar estado
        usuario.setActivo(nuevoEstado);

        // Realizar validaciones adicionales si es necesario
        if (!nuevoEstado) {
            // Lógica adicional para desactivación
            usuario.setFechaNacimiento(new Date()); // Ejemplo: actualizar fecha
        }

        // Guardar y forzar sincronización
        Usuario usuarioActualizado = usuarioRepository.saveAndFlush(usuario);

        return usuarioMapper.toDto(usuarioActualizado);
    }

    public List<UsuarioDto> ordenarPorNombre(String orden) {
        Sort sort = orden.equalsIgnoreCase("desc") ? Sort.by("nombre").descending() : Sort.by("nombre").ascending();
        return usuarioRepository.findAll(sort).stream().map(usuarioMapper::toDto).toList();
    }

    public List<UsuarioDto> ordenarPorFechaNacimiento(String orden) {
        Sort sort = orden.equalsIgnoreCase("desc") ? Sort.by("fechaNacimiento").descending() : Sort.by("fechaNacimiento").ascending();
        return usuarioRepository.findAll(sort).stream().map(usuarioMapper::toDto).toList();
    }

    public List<UsuarioDto> ordenarPorEmail(String orden) {
        Sort sort = orden.equalsIgnoreCase("desc") ? Sort.by("email").descending() : Sort.by("email").ascending();
        return usuarioRepository.findAll(sort).stream().map(usuarioMapper::toDto).toList();
    }

    public List<UsuarioDto> ordenarPorActivoYNombre(String orden) {
        Sort.Direction direccion = orden.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direccion, "activo", "nombre");
        return usuarioRepository.findAll(sort).stream().map(usuarioMapper::toDto).toList();
    }

    public List<UsuarioDto> ordenarPorLongitudTelefono(String orden) {
        Sort sort = orden.equalsIgnoreCase("desc")
                ? Sort.by(Sort.Order.desc("telefono").with(Sort.NullHandling.NULLS_LAST))
                : Sort.by(Sort.Order.asc("telefono").with(Sort.NullHandling.NULLS_FIRST));
        return usuarioRepository.findAll(sort).stream().map(usuarioMapper::toDto).toList();
    }


    /** Advanced method to find users with flexible filtering and sorting
    public List<UsuarioDto> findUsersWithFlexibleSearch(
            String nombre,
            String apellido,
            String email,
            Boolean activo,
            Date startBirthDate,
            Date endBirthDate,
            String sortBy,
            String sortDirection
    ) {
        // Create a probe (example) for dynamic querying
        UsuarioDto probe = new UsuarioDto();

        // Set optional search criteria
        if (nombre != null) probe.setNombre(nombre);
        if (apellido != null) probe.setApellido(apellido);
        if (email != null) probe.setEmail(email);
        if (activo != null) probe.setActivo(activo);

        // Create an ExampleMatcher for more flexible matching
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        // Create the Example
        Example<UsuarioDto> example = Example.of(probe, matcher);

        // Create a Sort object based on input parameters
        Sort sort = createSort(sortBy, sortDirection);

        // Execute the search with additional date range filtering
        return usuarioRepository.findAllDouble(example, sort).stream()
                .filter(usuario ->
                        (startBirthDate == null || usuario::getFechaNacimiento().after(startBirthDate)) &&
                                (endBirthDate == null || usuario.getFechaNacimiento().before(endBirthDate))
                )
                .toList();
    }

    // Helper method to create Sort object
    public Sort createSort(String sortBy, String sortDirection) {
        if (sortBy == null) {
            return Sort.unsorted();
        }

        // Map of valid sortable fields
        Set<String> validSortFields = Set.of(
                "id", "nombre", "apellido", "email",
                "fechaNacimiento", "activo"
        );

        // Validate sort field
        if (!validSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        // Create sort direction
        Sort.Direction direction = sortDirection != null &&
                sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        return Sort.by(direction, sortBy);
    }
     **/



    public List<UsuarioDto> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre).stream().map(usuarioMapper::toDto).toList();
    }

    public List<UsuarioDto> buscarPorNombreYApellido(String nombre, String apellido) {
        return usuarioRepository.findByNombreAndApellido(nombre, apellido).stream().map(usuarioMapper::toDto).toList();
    }

    public List<UsuarioDto> buscarPorNombreYDosApellidos(String nombre, String apellido) {
        return usuarioRepository.findByNombreAndApellidoContaining(nombre, apellido).stream().map(usuarioMapper::toDto).toList();
    }

//    public List<UsuarioDto> buscarPorEmailList(String email) {
//        return usuarioRepository.findByEmailList(email).stream().map(usuarioMapper::toDto).toList();
//    }

    public List<UsuarioDto> buscarPorAnioNacimiento(int anio) {
        return usuarioRepository.findByAnioNacimiento(anio).stream().map(usuarioMapper::toDto).toList();
    }
}