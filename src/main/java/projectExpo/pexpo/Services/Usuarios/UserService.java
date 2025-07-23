package projectExpo.pexpo.Services.Usuarios;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import projectExpo.pexpo.Config.Argon2PasswordEncoder;
import projectExpo.pexpo.Entities.Cargo.CargoEntity;
import projectExpo.pexpo.Entities.Usuario.UserEntity;
import projectExpo.pexpo.Exceptions.ExcepUsuarios.ExceptionsUsuarioNoEncontrado;
import projectExpo.pexpo.Models.DTO.Usuario.UserDTO;
import projectExpo.pexpo.Repositories.Cargo.CargoRepository;
import projectExpo.pexpo.Repositories.Usuario.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository repoUser;

    @Autowired
    private CargoRepository repoCargo;

    public List<UserDTO> getAllUsers(){
        List<UserEntity> usuarios = repoUser.findAll();
        return usuarios.stream()
                .map(this::convertirAUsuarioDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param userDto
     * @return retorna un objeto de tipo DTO
     */
    public UserDTO insertUser(UserDTO userDto){
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
            String contrasenaHash = argon2PasswordEncoder.HashPassword(userDto.getContrasena());
            userDto.setContrasena(contrasenaHash);
            UserEntity userEntity = convertirAUsuarioEntity(userDto);
            UserEntity usuarioGuardado = repoUser.save(userEntity);
            return convertirAUsuarioDTO(usuarioGuardado);
        }catch (Exception e){
            log.error("Error al registrar usuario: " + e.getMessage());
            throw new ExceptionsUsuarioNoEncontrado("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public UserDTO actualizarUsuario(Long id, UserDTO usuarioDto){
        //1. Verificar existencia
        UserEntity usuarioExistente = repoUser.findById(id).orElseThrow(() -> new ExceptionsUsuarioNoEncontrado("Usuario no encontrado"));

        //2. Actualizar campos
        usuarioExistente.setNombre(usuarioDto.getNombre());
        usuarioExistente.setApellido(usuarioDto.getApellido());
        usuarioExistente.setIdGrupoExpo(usuarioDto.getIdGrupoExpo());
        usuarioExistente.setIdRol(usuarioDto.getIdRol());
        usuarioExistente.setCorreo(usuarioDto.getCorreo());

        //3. Actualizar relación con Cargo
        if (usuarioDto.getIdCargo() != null){
            CargoEntity cargo = repoCargo.findById(usuarioDto.getIdCargo())
                    .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado con ID proporcionado"));
            usuarioExistente.setCargo(cargo);
        }else {
            usuarioExistente.setCargo(null);
        }

        //4. Guardar cambios
        UserEntity usuarioActualizado = repoUser.save(usuarioExistente);

        //5. Convertir a DTO
        return convertirAUsuarioDTO(usuarioActualizado);
    }

    public boolean removerUsuario(Long id){
        try{
            //Se valida la existencia del usuario previamente a la eliminación
            UserEntity objUsuario = repoUser.findById(id).orElse(null);
            //Si objUsuario existe se procede a eliminar
            if (objUsuario != null){
                repoUser.deleteById(id);
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
    //1. Se reciben todos los datos en formato Entity
    private UserDTO convertirAUsuarioDTO(UserEntity usuario) {
        //2. Se crea un objeto de tipo dto
        UserDTO dto = new UserDTO();
        //3. Se guardan todos los valores Entity en los atributos DTO
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setIdGrupoExpo(usuario.getIdGrupoExpo());
        dto.setIdRol(usuario.getIdRol());
        dto.setCorreo(usuario.getCorreo());
        dto.setContrasena(usuario.getContrasena());
        /**
         * 4. Si el cargo es diferente de null se procede a guardar el Cargo en el DTO
         * En caso el cargo no sea diferente de null pues en el idCargo se guardará null
         */
        if (usuario.getCargo() != null){
            dto.setNombreCargo(usuario.getCargo().getCargo());
            dto.setIdCargo(usuario.getCargo().getId());
        }else{
            dto.setNombreCargo("Sin cargo asignado");
            dto.setIdCargo(null);
        }

        //5. Se retornan los valores en formato dto.
        return dto;
    }

    // Metodo para convertir DTO a Entidad
    // 1. Se reciben los datos en formato DTO
    private UserEntity convertirAUsuarioEntity(UserDTO dto) {
        /** 2. Se crea el objeto usuario de tipo Entity, es decir se crea como una "CAJA" de tipo Entity que guardará
         * todos los datos de tipo DTO.
         */
        UserEntity usuario = new UserEntity();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setIdGrupoExpo(dto.getIdGrupoExpo());
        usuario.setIdRol(dto.getIdRol());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(dto.getContrasena());

        /**
         * Si el idCargo del DTO no posee ningún valor quiere decir que es NULL, de lo contrario será diferente de NULL
         * En la condición preguntamos si el idCargo es diferente de NULL
         */
        if (dto.getIdCargo() != null){
            /**Cuando entre a la condición, se crea un objeto de tipo CargoEntity que buscará el ID, en caso no sea encontrado
             * se procede a lanzar una excepción indicando que no se encontró el cargo.
             */
            CargoEntity cargo = repoCargo.findById(dto.getIdCargo())
                    .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado con ID: " + dto.getIdCargo()));
            //Si el idCargo existe se procede a guardar el idCargo del DTO sobre idCargo del Entity
            usuario.setCargo(cargo); //Usa setCargo() en lugar de setIdCargo()
        }
        return usuario;
    }

}
