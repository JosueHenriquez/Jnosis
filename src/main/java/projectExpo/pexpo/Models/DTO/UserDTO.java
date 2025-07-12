package projectExpo.pexpo.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class UserDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Getter @Setter
    private long idGrupoExpo;

    @Getter @Setter
    private long idRol;

    @Email(message = "Debe ser un correo valido")
    private String correo;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @NotNull(message = "El ID de cargo no puede ser nulo")
    @Positive(message = "El ID de cargo debe ser positivo")
    private Long idCargo;

    //Campo adicional para mostrar el nombre de cargo, este campo como tal no existe en la tabla usuario.
    private String nombreCargo;
}
