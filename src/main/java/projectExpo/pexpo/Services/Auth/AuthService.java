package projectExpo.pexpo.Services.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projectExpo.pexpo.Entities.Usuario.UserEntity;
import projectExpo.pexpo.Repositories.Login.LoginRepository;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private LoginRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean LogIn(String correo, String contrasena){
        //
        Optional<UserEntity> usuario = repo.findByCorreo(correo);
        return usuario.isPresent() && passwordEncoder.matches(contrasena, usuario.get().getContrasena());
    }
}
