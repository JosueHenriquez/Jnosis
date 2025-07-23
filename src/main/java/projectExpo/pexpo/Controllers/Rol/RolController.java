package projectExpo.pexpo.Controllers.Rol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectExpo.pexpo.Models.DTO.Rol.RolDTO;
import projectExpo.pexpo.Services.Rol.RolService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RolController {

    @Autowired
    private RolService service;

    @GetMapping("/obtenerRoles")
    public List<RolDTO> obtenerRoles(){
        return service.consultarRoles();
    }
}
