package projectExpo.pexpo.Services.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projectExpo.pexpo.Config.Argon2PasswordEncoder;
import projectExpo.pexpo.Entities.Usuario.UserEntity;
import projectExpo.pexpo.Repositories.Login.LoginRepository;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private LoginRepository repo;

    public boolean LogIn(String correo, String contrasena){
        Argon2PasswordEncoder objHash = new Argon2PasswordEncoder();
        Optional<UserEntity> usuario = repo.findByCorreo(correo);
        if (!usuario.isPresent()){
            return false;
        }
        String hashBD = usuario.get().getContrasena();
        return objHash.decodePassword(hashBD, contrasena);
    }
}
