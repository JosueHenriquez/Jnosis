package projectExpo.pexpo.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

/**
 * Para poder utilizar DatatypeConverte utiliza agrega la dependecia en el pom.xml
 * <dependency>
 *     <groupId>jakarta.xml.bind</groupId>
 *     <artifactId>jakarta.xml.bind-api</artifactId>
 *     <version>3.0.0</version>
 * </dependency>
 */
@Component
public class JWTUtils {

    @Value("${security.jwt.secret}") String jwtSecreto;
    @Value("${security.jwt.expiration}") long expiracionMs;
    @Value("${security.jwt.issuer}") private String issuer;

    private final Logger log = LoggerFactory
            .getLogger(JWTUtils.class);

    public String create(String id, String nombre){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecreto);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(nombre).setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        if (expiracionMs >= 0) {
            long expMillis = nowMillis + expiracionMs;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Method to validate and read the JWT
     *
     * @param jwt
     * @return
     */
    public String getValue(String jwt) {
        // This line will throw an exception if it is not a signed JWS (as
        // expected)
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecreto))
                .parseClaimsJws(jwt).getBody();

        return claims.getSubject();
    }

    /**
     * Method to validate and read the JWT
     *
     * @param jwt
     * @return
     */
    public String getKey(String jwt) {
        // This line will throw an exception if it is not a signed JWS (as
        // expected)
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecreto))
                .parseClaimsJws(jwt).getBody();

        return claims.getId();
    }

    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecreto))
                .parseClaimsJws(jwt)
                .getBody();
    }
}
