package projectExpo.pexpo.Entities.Rol;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @EqualsAndHashCode @ToString
@Table(name = "TBROL")
public class RolEntity {

    @Id
    @Column(name = "IDROL")
    private Long id;
    @Column(name = "ROL")
    private String rol;
}
