package projectExpo.pexpo.Entities.Usuario;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import projectExpo.pexpo.Entities.Cargo.CargoEntity;

@Entity
@Table(name = "TBUSUARIO")
@Getter @Setter @ToString @EqualsAndHashCode
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    @Column(name = "IDUSUARIO")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "IDGRUPOEXPO")
    private long idGrupoExpo;

    @Column(name = "IDROL")
    private long idRol;

    @Column(name = "CORREO", unique = true)
    private String correo;

    @Column(name = "CONTRASENA")
    private String contrasena;

    /**
     * Se define que el atributo cargo es de tipo CargoEntity y que este campo
     * con JoinColumn(name -> apunta hacia la llave foranea
     * referencedColumnName -> apunta hacia la llave primaria de la tabla Cargo
     */
    @ManyToOne
    @JoinColumn(name = "IDCARGO", referencedColumnName = "IDCARGO")
    private CargoEntity cargo;
}