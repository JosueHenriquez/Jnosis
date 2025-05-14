package projectExpo.pexpo.Models.DAO.Usuario;

import projectExpo.pexpo.Models.DTO.DTOUsuario;

import java.util.List;

public interface InterfaceUsuario {
    public List<DTOUsuario> datosUsuarios();
    public void registrarUsuario(DTOUsuario usuario);
    public void actualizarUsuario(Long id, DTOUsuario usuario);
    public void removerUsuario(Long id);
}
