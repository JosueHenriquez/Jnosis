package projectExpo.pexpo.Exceptions.ExcepUsuarios;

public class ExceptionsUsuarioNoEncontrado extends RuntimeException{

    public ExceptionsUsuarioNoEncontrado(String message) {
        super(message);
    }

    public ExceptionsUsuarioNoEncontrado(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionsUsuarioNoEncontrado(Throwable cause) {
        super(cause);
    }
}
