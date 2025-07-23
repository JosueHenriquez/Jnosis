package projectExpo.pexpo.Controllers.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projectExpo.pexpo.Exceptions.ExcepUsuarios.ExcepcionDatosDuplicados;
import projectExpo.pexpo.Exceptions.ExcepUsuarios.ExceptionsUsuarioNoEncontrado;
import projectExpo.pexpo.Models.DTO.Usuario.UserDTO;
import projectExpo.pexpo.Services.Usuarios.UserService;

import java.time.Instant;
import java.util.HashMap;
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
public class UserController {

    /**
     * Imagina que estás en una cocina y necesitas un cuchillo. Tienes dos opciones:
     * Crearlo tú mismo: Forjar el metal, hacer el mango... (complejo)
     * Pedir que te lo den: "¡Oye, necesito un cuchillo!" (sencillo)
     * @Autowired es como pedir que te den el cuchillo. Le estás diciendo a Spring:
     * "Oye Spring, necesito un objeto de tipo InterfacfeLogin. Por favor, búscalo o créalo
     * y dámelo automáticamente."
     */
    @Autowired
    private UserService acceso;
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
    public List<UserDTO> datosUsuarios() {
        return acceso.getAllUsers();
    }

    @PostMapping("/ingresarUsuario")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@Valid @RequestBody UserDTO usuario, HttpServletRequest request){
        try{
            //Intento de guardar usuario
            UserDTO respuesta = acceso.insertUser(usuario);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Inserción incorrecta",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Datos del usuario inválidos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status","sucess",
                    "data",respuesta));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al registrar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/modificarUsuario/{id}")
    public ResponseEntity<?> modificarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO usuario,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            UserDTO usuarioActualizado = acceso.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        }

        catch (ExceptionsUsuarioNoEncontrado e){
            return ResponseEntity.notFound().build();
        }

        catch (ExcepcionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("error", "Datos duplicados","campo", e.getCampoDuplicado())
            );
        }
    }

    // Mapea este metodo a una petición DELETE con un parámetro de ruta {id}
    @DeleteMapping("/eliminarusuario/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        try {
            // Intenta eliminar el usuario usando el servicio 'acceso'
            // Si removerUsuario retorna false (no encontró el usuario)
            if (!acceso.removerUsuario(id)) {
                // Retorna una respuesta 404 (Not Found) con información detallada
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        // Agrega un header personalizado
                        .header("X-Mensaje-Error", "Usuario no encontrado")
                        // Cuerpo de la respuesta con detalles del error
                        .body(Map.of(
                                "error", "Not found",  // Tipo de error
                                "mensaje", "El usuario no ha sido encontrado",  // Mensaje descriptivo
                                "timestamp", Instant.now().toString()  // Marca de tiempo del error
                        ));
            }

            // Si la eliminación fue exitosa, retorna 200 (OK) con mensaje de confirmación
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",  // Estado de la operación
                    "message", "Usuario eliminado exitosamente"  // Mensaje de éxito
            ));

        } catch (Exception e) {
            // Si ocurre cualquier error inesperado, retorna 500 (Internal Server Error)
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",  // Indicador de error
                    "message", "Error al eliminar el usuario",  // Mensaje general
                    "detail", e.getMessage()  // Detalles técnicos del error (para debugging)
            ));
        }
    }
}
