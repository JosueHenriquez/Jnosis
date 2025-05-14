package projectExpo.pexpo.Models.DAO.Login;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Models.DAO.Usuario.InterfaceLogin;
import projectExpo.pexpo.Models.DTO.DTOUsuario;
import projectExpo.pexpo.Services.Argon2Hash;

import java.util.List;

@Repository
@Transactional //Librería dependiente de jakarta.transaction-api
public class DAOLogin implements InterfaceLogin {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public DTOUsuario iniciarSesion(DTOUsuario log){
        String query = "FROM DTOUsuario WHERE correo = :email";
        List<DTOUsuario> lista = entityManager.createQuery(query)
                .setParameter("email", log.getCorreo())
                .getResultList();
        if (lista.isEmpty()){
            System.out.println("Usuario no encontrado relacionado con el correo: " + log.getContrasena());
            return null;
        }
        Argon2Hash argon2Hash = new Argon2Hash();
        String passwordHashed = lista.get(0).getContrasena();
        if(argon2Hash.verifierHash(passwordHashed, log.getContrasena())){
            return lista.get(0);
        }
        return null;
    }
}
