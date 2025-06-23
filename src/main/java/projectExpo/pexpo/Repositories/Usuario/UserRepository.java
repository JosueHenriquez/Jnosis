package projectExpo.pexpo.Repositories.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Entities.Usuario.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{ }
