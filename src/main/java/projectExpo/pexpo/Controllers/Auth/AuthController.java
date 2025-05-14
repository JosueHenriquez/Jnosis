package projectExpo.pexpo.Controllers.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectExpo.pexpo.Models.DAO.Usuario.InterfaceLogin;
import projectExpo.pexpo.Models.DTO.DTOUsuario;
import projectExpo.pexpo.Utils.JWTUtils;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private InterfaceLogin ILogin;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody DTOUsuario DTOlog, HttpServletResponse response, HttpServletRequest request){

        if(ILogin.iniciarSesion(DTOlog) != null){
            String token = jwtUtils.create(String.valueOf(DTOlog.getId()), DTOlog.getNombre());

            //Crear la cookie
            Cookie cookie = new Cookie("loginToken", token); //Nombre y valor de la cookie
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(7*24*60*60);

            response.addCookie(cookie);
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales inválidas");
    }
}
