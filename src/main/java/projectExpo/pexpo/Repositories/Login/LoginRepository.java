package projectExpo.pexpo.Repositories.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Entities.Usuario.EntityUsuario;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<EntityUsuario, Long> {

    Optional<EntityUsuario> findByCorreo(String correo);
}
