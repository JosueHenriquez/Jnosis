package projectExpo.pexpo.Models.DTO;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //Indica que hará referencia a la base de datos
@Table(name = "TBUSUARIO")
@ToString @EqualsAndHashCode

public class DTOUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    @Getter @Setter @Column(name = "IDUSUARIO")
    private Long id;
    @Getter @Setter @Column(name = "NOMBRE")
    private String nombre;
    @Getter @Setter @Column(name = "APELLIDO")
    private String apellido;
    @Getter @Setter @Column(name = "IDGRUPOEXPO")
    private long idGrupoExpo;
    @Getter @Setter @Column(name = "IDROL")
    private long idRol;
    @Getter @Setter @Column(name = "CORREO")
    private String correo;
    @Getter @Setter @Column(name = "CONTRASENA")
    private String contrasena;
    @Getter @Setter @Column(name = "IDCARGO")
    private long idCargo;
}
