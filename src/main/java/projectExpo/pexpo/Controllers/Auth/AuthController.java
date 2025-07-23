package projectExpo.pexpo.Controllers.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import projectExpo.pexpo.Models.DTO.Usuario.UserDTO;
import projectExpo.pexpo.Services.Auth.AuthService;
import projectExpo.pexpo.Utils.JWTUtils;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthService serviceAuth;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO DTOlog, HttpServletResponse response, HttpServletRequest request){
        //Se valida que los datos no esten vacíos
        if (!(DTOlog.getCorreo().isEmpty() || DTOlog.getContrasena().isEmpty())){
            /*
             * Se intenta iniciar sesión enviando el correo y la contraseña, en caso los valores
             * sean incorrectos retornará un FALSE pero se niega la respuesta para que sea verdadero y
             * mostrar el error. En caso la respuesta sea TRUE, se niega la respuesta para que no muestre
             * el mensaje de error y continue con la ejecución del código.
             */
            if(!serviceAuth.LogIn(DTOlog.getCorreo(), DTOlog.getContrasena())){
                return ResponseEntity.status(401).body("Credenciales incorrectas");
            }
            addTokenCookie(response, DTOlog);
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(401).body("Error, verifica que hayas compartida todas las credenciales necesarias para iniciar sesión.");
    }

    /**
     * Se genera el token y se guarda en la Cookie
     * @param response
     * @param DTOlog
     */
    void addTokenCookie(HttpServletResponse response, UserDTO DTOlog){
        String token = jwtUtils.create(String.valueOf(DTOlog.getId()), DTOlog.getNombre());
        //Crear la cookie
        Cookie cookie = new Cookie("authToken", token); //Nombre y valor de la cookie
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400); //Tiempo definido en segundos
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        //1. Eliminar la cookie del cliente
        Cookie cookie = new Cookie("authToken", null); //Nombre debe coincidir con la cookie del login
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().body("Sesión cerrada exitosamente");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = jwtUtils.extractTokenFromRequest(request);
        if (token == null || !jwtUtils.validate(token)) {
            return ResponseEntity.status(401).body("Token inválido o ausente");
        }
        return ResponseEntity.ok("Token válido");
    }
}
