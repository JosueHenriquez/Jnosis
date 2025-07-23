package projectExpo.pexpo.Controllers.Cargo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectExpo.pexpo.Models.DTO.Cargo.CargoDTO;
import projectExpo.pexpo.Services.Cargo.CargoService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CargoController {

    @Autowired
    private CargoService service;

    @GetMapping("/obtenerCargos")
    public List<CargoDTO> obtenerCargos(){
        return service.consultarCargos();
    }
}
