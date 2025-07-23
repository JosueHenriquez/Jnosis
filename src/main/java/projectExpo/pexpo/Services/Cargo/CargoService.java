package projectExpo.pexpo.Services.Cargo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectExpo.pexpo.Entities.Cargo.CargoEntity;
import projectExpo.pexpo.Entities.Rol.RolEntity;
import projectExpo.pexpo.Models.DTO.Cargo.CargoDTO;
import projectExpo.pexpo.Models.DTO.Rol.RolDTO;
import projectExpo.pexpo.Repositories.Cargo.CargoRepository;
import projectExpo.pexpo.Repositories.Rol.RolRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargoService {

    @Autowired
    CargoRepository repo;

    public List<CargoDTO> consultarCargos() {
        List<CargoEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private CargoDTO convertirADTO(CargoEntity cargoEntity) {
        CargoDTO dto = new CargoDTO();
        dto.setId(cargoEntity.getId());
        dto.setNombreCargo(cargoEntity.getCargo());
        return dto;
    }
}
