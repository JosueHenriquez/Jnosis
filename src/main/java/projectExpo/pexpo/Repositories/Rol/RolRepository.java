package projectExpo.pexpo.Repositories.Rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Entities.Rol.RolEntity;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {
}
