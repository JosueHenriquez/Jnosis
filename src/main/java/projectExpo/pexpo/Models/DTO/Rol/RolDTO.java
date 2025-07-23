package projectExpo.pexpo.Models.DTO.Rol;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RolDTO {
    private Long idRol;
    @NotBlank
    private String rol;
}
