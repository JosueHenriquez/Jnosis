// Declaración del paquete al que pertenece la clase
package projectExpo.pexpo.Utils;

// Importaciones necesarias para el manejo de JWT
import io.jsonwebtoken.*;           // Clases principales para trabajar con JWT
import io.jsonwebtoken.io.Decoders; // Utilidades para decodificación
import io.jsonwebtoken.security.Keys; // Utilidades para generación de claves seguras
import org.slf4j.Logger;            // Para logging
import org.slf4j.LoggerFactory;     // Para inicialización del logger
import org.springframework.beans.factory.annotation.Value; // Para inyección de propiedades
import org.springframework.stereotype.Component; // Para marcar como componente Spring
import javax.crypto.SecretKey;      // Para manejo de claves criptográficas
import java.util.Base64;            // Para codificación/decodificación Base64 (aunque no se usa directamente)
import java.util.Date;              // Para manejo de fechas

// Anotación que marca esta clase como un componente de Spring
@Component
public class JWTUtils {

    // Inyección del secreto para firmar los JWT desde application.properties
    @Value("${security.jwt.secret}")
    private String jwtSecreto;

    // Inyección del tiempo de expiración en milisegundos
    @Value("${security.jwt.expiration}")
    private long expiracionMs;

    // Inyección del emisor (issuer) del token
    @Value("${security.jwt.issuer}")
    private String issuer;

    // Logger para registrar eventos y errores
    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * Método para crear un JWT
     * @param id Identificador único para el token
     * @param nombre Sujeto (subject) del token
     * @return String con el JWT firmado
     */
    public String create(String id, String nombre) {
        // Decodifica el secreto Base64 y crea una clave HMAC-SHA segura
        SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto));

        // Obtiene la fecha actual y calcula la fecha de expiración
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiracionMs);

        // Construye el JWT con todos sus componentes
        return Jwts.builder()
                .setId(id)                      // ID único (JWT ID)
                .setIssuedAt(now)              // Fecha de emisión
                .setSubject(nombre)             // Sujeto (usuario)
                .setIssuer(issuer)              // Emisor del token
                .setExpiration(expiracionMs >= 0 ? expiration : null) // Expiración (si es >= 0)
                .signWith(signingKey, SignatureAlgorithm.HS256) // Firma con algoritmo HS256
                .compact();                    // Convierte a String compacto
    }

    /**
     * Obtiene el subject (nombre) del JWT
     * @param jwt Token JWT como String
     * @return String con el subject del token
     */
    public String getValue(String jwt) {
        // Parsea los claims y devuelve el subject
        Claims claims = parseClaims(jwt);
        return claims.getSubject();
    }

    /**
     * Obtiene el ID del JWT
     * @param jwt Token JWT como String
     * @return String con el ID del token
     */
    public String getKey(String jwt) {
        // Parsea los claims y devuelve el ID
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    /**
     * Parsea y valida un token JWT
     * @param jwt Token a validar
     * @return Claims (reclamaciones) del token
     * @throws ExpiredJwtException Si el token está expirado
     * @throws MalformedJwtException Si el token está mal formado
     */
    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }

    /**
     * Método privado para parsear los claims de un JWT
     * @param jwt Token a parsear
     * @return Claims del token
     */
    private Claims parseClaims(String jwt) {
        // Configura el parser con la clave de firma y parsea el token
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecreto))) // Clave de firma
                .build()                     // Construye el parser
                .parseClaimsJws(jwt)        // Parsea y valida el token
                .getBody();                 // Obtiene los claims (cuerpo del token)
    }
}