package projectExpo.pexpo.Entities.Cargo;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import projectExpo.pexpo.Entities.Usuario.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBCARGO")
@Getter @Setter @ToString @EqualsAndHashCode
public class CargoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cargo")
    @SequenceGenerator(name = "seq_cargo", sequenceName = "seq_cargo", allocationSize = 1)
    @Column(name = "IDCARGO")
    private Long Id;

    @Column(name = "CARGO")
    private String Cargo;

    /**
     * Se mapea que la relación apuntará hacia el atributo Cargo de UserEntity
     * ya que se podrán tener un cargo en muchos usuarios.
     */
    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
    private List<UserEntity> usuarios = new ArrayList<>();
}
