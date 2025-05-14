package projectExpo.pexpo.Models.DAO.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projectExpo.pexpo.Models.DTO.DTOUsuario;
import java.util.List;

@Repository
@Transactional //Librería dependiente de jakarta.transaction-api
public class DAOUsuario implements InterfaceUsuario {

    /**
     * Para crear el objeto entityManager se debe agregar en el pom.xml
     * la dependecia:
     * <dependency>
     * 			<groupId>org.springframework.boot</groupId>
     * 			<artifactId>spring-boot-starter-data-jpa</artifactId>
 * 		</dependency>
     * 	el objeto entityManager se utilizará para crear una vinculación
     * 	entre la API y la base de datos, el objeto se encargará del
     * 	traslado de la información.
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<DTOUsuario> datosUsuarios() {
        String query = "FROM DTOUsuario";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void registrarUsuario(DTOUsuario usuario){
        entityManager.merge(usuario);
    }

    @Override
    @Transactional
    public void actualizarUsuario(Long id, DTOUsuario usuarioActualizado) {
        DTOUsuario usuarioExistente = entityManager.find(DTOUsuario.class, id);
        if (usuarioExistente != null) {
            // Actualiza solo los campos no nulos
            if (usuarioActualizado.getNombre() != null) {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
            }
            if (usuarioActualizado.getApellido() != null) {
                usuarioExistente.setApellido(usuarioActualizado.getApellido());
            }
            // Continúa con los demás campos...
            if (usuarioActualizado.getIdGrupoExpo() != 0) {
                usuarioExistente.setIdGrupoExpo(usuarioActualizado.getIdGrupoExpo());
            }
            if (usuarioActualizado.getIdRol() != 0) {
                usuarioExistente.setIdRol(usuarioActualizado.getIdRol());
            }
            if (usuarioActualizado.getCorreo() != null) {
                usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
            }
            if (usuarioActualizado.getContrasena() != null) {
                usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
            }
            if (usuarioActualizado.getIdCargo() != 0) {
                usuarioExistente.setIdCargo(usuarioActualizado.getIdCargo());
            }
            entityManager.merge(usuarioExistente);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    /*
    @Override
    @Transactional
    public void actualizarUsuario(Long id, DTOUsuario usuarioActualizado) {
        usuarioRepository.findById(id)
                .ifPresentOrElse(usuarioExistente -> {
                    // Copia propiedades no nulas
                    BeanUtils.copyProperties(usuarioActualizado, usuarioExistente,
                            "id", "nullFields");
                    usuarioRepository.save(usuarioExistente);
                }, () -> {
                    throw new RuntimeException("Usuario no encontrado con ID: " + id);
                });
    }*/

    @Override
    public void removerUsuario(Long id){
        DTOUsuario usuario = entityManager.find(DTOUsuario.class, id);
        entityManager.remove(usuario);
    }
}
