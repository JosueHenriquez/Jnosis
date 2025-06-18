package projectExpo.pexpo.Repositories.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Entities.Usuario.EntityUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<EntityUsuario, Long>{ }
