package projectExpo.pexpo.Services.Usuarios;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import projectExpo.pexpo.Config.Argon2PasswordEncoder;
import projectExpo.pexpo.Entities.Usuario.UserEntity;
import projectExpo.pexpo.Exceptions.ExcepUsuarios.ExceptionsUsuarioNoEncontrado;
import projectExpo.pexpo.Models.DTO.UserDTO;
import projectExpo.pexpo.Repositories.Usuario.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public List<UserDTO> getAllUsers(){
        List<UserEntity> usuarios = repo.findAll();
        return usuarios.stream()
                .map(this::convertirAUsuarioDTO)
                .collect(Collectors.toList());
    }

    public UserDTO registrarUsuario(UserDTO userDto){
        /**
         * Se valida si el usuario es nulo, es decir, si contiene información.
         * Se valida si la contraseña es nula o vacía
         * De cumplirse cualquiera de las dos condiciones anteriores se lanza la excepción IlegalArgumentException
         */
        if (userDto == null || userDto.getContrasena() == null || userDto.getContrasena().isEmpty()){
            throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos o vacios");
        }
        try {
            /**
             * Si pasa la validación anterior, se crea un objeto de tipo Argon2Hash llamado objHash
             * La clase Argon2Hash posee un atributo password, entonces a ese atributo le damos la contraseña sin encriptar que proviene
             */
            Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();
            //Se asigna nuevamente al atributo de contraseña de la clase usuario la contraseña ya encriptada.
            String contrasenaHash = argon2PasswordEncoder.encode(userDto.getContrasena());
            userDto.setContrasena(contrasenaHash);
            UserEntity userEntity = convertirAUsuarioEntity(userDto);
            UserEntity usuarioGuardado = repo.save(userEntity);
            return convertirAUsuarioDTO(usuarioGuardado);
        }catch (Exception e){
            log.error("Error al registrar usuario: " + e.getMessage());
            throw new ExceptionsUsuarioNoEncontrado("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public UserDTO actualizarUsuario(Long id, UserDTO usuario){
        //Verificar existencia
        UserEntity usuarioExistente = repo.findById(id).orElseThrow(() -> new ExceptionsUsuarioNoEncontrado("Usuario no encontrado"));

        //Actualizar campos
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setIdGrupoExpo(usuario.getIdGrupoExpo());
        usuarioExistente.setIdRol(usuario.getIdRol());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setIdCargo(usuario.getIdCargo());

        UserEntity usuarioActualizado = repo.save(usuarioExistente);
        return convertirAUsuarioDTO(usuarioActualizado);
    }

    public boolean removerUsuario(Long id){
        try{
            //Se valida la existencia del usuario previamente a la eliminación
            UserEntity objUsuario = repo.findById(id).orElse(null);
            //Si objUsuario existe se procede a eliminar
            if (objUsuario != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Usuario no encontrado.");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro usuario con ID: " + id + " para eliminar.", 1);
        }
    }

    // Metodo para convertir Entidad a DTO
    private UserDTO convertirAUsuarioDTO(UserEntity usuario) {
        UserDTO dto = new UserDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setIdGrupoExpo(usuario.getIdGrupoExpo());
        dto.setIdRol(usuario.getIdRol());
        dto.setCorreo(usuario.getCorreo());
        dto.setContrasena(usuario.getContrasena());
        dto.setIdCargo(usuario.getIdCargo());
        return dto;
    }

    // Metodo para convertir DTO a Entidad
    private UserEntity convertirAUsuarioEntity(UserDTO dto) {
        UserEntity usuario = new UserEntity();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setIdGrupoExpo(dto.getIdGrupoExpo());
        usuario.setIdRol(dto.getIdRol());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(dto.getContrasena());
        usuario.setIdCargo(dto.getIdCargo());
        return usuario;
    }

}
