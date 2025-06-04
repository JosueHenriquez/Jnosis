package projectExpo.pexpo.Controllers.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectExpo.pexpo.Models.DAO.Usuario.InterfaceUsuario;
import projectExpo.pexpo.Models.DTO.DTOUsuario;
import projectExpo.pexpo.Services.Argon2Hash;
import java.util.List;
import java.util.Map;

/**
 * El @RestController es como un "mesero digital" en tu aplicación Spring que se encarga de:
 * Recibir pedidos (peticiones HTTP)
 * Procesarlos (ejecutar tu lógica)
 * Entregar respuestas (en formato JSON/XML)
 */
@RestController
@RequestMapping("/api") //Todas las URLs (endPoint, se anteceden de la palabra api, por ejemplo api/usuarios)
public class UsuarioController {

    /**
     * Imagina que estás en una cocina y necesitas un cuchillo. Tienes dos opciones:
     * Crearlo tú mismo: Forjar el metal, hacer el mango... (complejo)
     * Pedir que te lo den: "¡Oye, necesito un cuchillo!" (sencillo)
     * @Autowired es como pedir que te den el cuchillo. Le estás diciendo a Spring:
     * "Oye Spring, necesito un objeto de tipo InterfacfeLogin. Por favor, búscalo o créalo
     * y dámelo automáticamente."
     */
    @Autowired
    private InterfaceUsuario acceso;

    /**
     * @GetMapping - La "dirección" del endpoint
     * Qué hace: Define la ruta URL y el tipo de petición HTTP que atenderá este método
     * Partes:
     * value = "api/usuarios": La ruta relativa (se accedería con http://tudominio.com/api/usuarios)
     * method = RequestMethod.GET: Solo aceptará peticiones GET (consultas)
     * (Equivalente moderno sería usar @GetMapping("api/usuarios") que es más corto)
     * Retorna una lista de objetos de tipo DTOLogin
     *
     * Proceso de ejecución:
     * Un cliente hace una petición GET a /usuarios
     * Spring dirige la petición a este metodo
     * El metodo obtiene los datos a través de acceso.datosUsuarios()
     * Spring convierte la lista a JSON automáticamente
     * Se devuelve la respuesta HTTP con los datos
     * @return
     */
    @GetMapping("/usuarios")
    public List<DTOUsuario> datosUsuarios() {
        //System.out.println("Entre al enpoint");
        return acceso.datosUsuarios();
    }

    @PostMapping("/ingresarUsuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody DTOUsuario usuario, HttpServletRequest request){
        try{
            System.out.println("Entre al enpoint");
            Argon2Hash objHash = new Argon2Hash();
            objHash.setPassword(usuario.getContrasena());
            usuario.setContrasena(objHash.PasswordHash());

            acceso.registrarUsuario(usuario);
            return ResponseEntity.ok().body(Map.of(
                    "status","sucess",
                    "message","Usuario registrado exitosamente"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al actualizar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/modificarUsuario/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable Long id, @RequestBody DTOUsuario usuario){
        try{
            acceso.actualizarUsuario(id, usuario);
            return ResponseEntity.ok().body(Map.of(
                    "status", "Completado",
                    "message", "Usuario actualizado exitosamente"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al actualizar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/eliminarusuario/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        try{
            acceso.removerUsuario(id);
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Usuario eliminado existosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message","Error al eliminar el usuario",
                    "detail", e.getMessage()
            ));
        }
    }
}
