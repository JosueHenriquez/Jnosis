package projectExpo.pexpo.Services.Rol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectExpo.pexpo.Entities.Rol.RolEntity;
import projectExpo.pexpo.Models.DTO.Rol.RolDTO;
import projectExpo.pexpo.Repositories.Rol.RolRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    @Autowired
    RolRepository repo;

    public List<RolDTO> consultarRoles() {
        List<RolEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private RolDTO convertirADTO(RolEntity rolEntity) {
        RolDTO dto = new RolDTO();
        dto.setIdRol(rolEntity.getId());
        dto.setRol(rolEntity.getRol());
        return dto;
    }
}
