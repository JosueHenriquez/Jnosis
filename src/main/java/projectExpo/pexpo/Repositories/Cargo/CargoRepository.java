package projectExpo.pexpo.Repositories.Cargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Entities.Cargo.CargoEntity;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity, Long> {

}
