package projectExpo.pexpo.Models.DTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class DTOUsuario {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Getter @Setter @Column(name = "IDGRUPOEXPO")
    private long idGrupoExpo;

    @Getter @Setter @Column(name = "IDROL")
    private long idRol;

    @Email(message = "Debe ser un correo valido")
    private String correo;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    private long idCargo;
}
