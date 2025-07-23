package projectExpo.pexpo.Models.DTO.Cargo;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString @EqualsAndHashCode
public class CargoDTO {

    private Long id;
    @NotBlank(message = "El nombre del cargo es obligatorio.")
    private String nombreCargo;
}
