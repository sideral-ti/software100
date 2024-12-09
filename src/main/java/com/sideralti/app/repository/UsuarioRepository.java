package com.sideralti.app.repository;

import com.sideralti.app.dto.UsuarioDto;
import com.sideralti.app.model.Usuario;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Long countByActivoTrue();
    Long countByActivoFalse();

    // Método de búsqueda personalizado
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAll(Sort sort);

   // Optional<Object> findAllDouble(Example<UsuarioDto> example, Sort sort);

    List<Usuario> findByNombre(String nombre);

    List<Usuario> findByNombreAndApellido(String nombre, String apellido);

    List<Usuario> findByNombreAndApellidoContaining(String nombre, String apellido);

   // List<Usuario> findByEmailList(String email);

    @Query("SELECT u FROM Usuario u WHERE EXTRACT(YEAR FROM u.fechaNacimiento) = :anio")
    List<Usuario> findByAnioNacimiento(@Param("anio") int anio);
}
