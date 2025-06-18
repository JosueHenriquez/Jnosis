package projectExpo.pexpo.Config;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Argon2PasswordEncoder implements PasswordEncoder {
    // Configuración recomendada para Argon2id
    private static final int ITERATIONS = 1;
    private static final int MEMORY = 1024; //
    private static final int PARALLELISM = 1;
    private static final int SALT_LENGTH = 16; // 16 bytes
    private static final int HASH_LENGTH = 16; // 16 bytes

    @Override
    public String encode(CharSequence rawPassword) {
        // Generar un salt aleatorio
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        // Configurar parámetros de Argon2id
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(ITERATIONS)
                .withMemoryAsKB(MEMORY)
                .withParallelism(PARALLELISM);

        // Generar el hash
        byte[] hash = new byte[HASH_LENGTH];
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        generator.generateBytes(rawPassword.toString().toCharArray(), hash);

        // Combinar salt y hash para almacenarlos en BD
        byte[] combined = new byte[SALT_LENGTH + HASH_LENGTH];
        System.arraycopy(salt, 0, combined, 0, SALT_LENGTH);
        System.arraycopy(hash, 0, combined, SALT_LENGTH, HASH_LENGTH);

        String cadena = Base64.getEncoder().encodeToString(combined);
        return cadena;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Decodificar el string Base64
        byte[] combined = Base64.getDecoder().decode(encodedPassword);

        // Extraer salt y hash
        byte[] salt = new byte[SALT_LENGTH];
        byte[] hash = new byte[HASH_LENGTH];
        System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
        System.arraycopy(combined, HASH_LENGTH, hash, 0, HASH_LENGTH);

        // Configurar parámetros de Argon2id con el salt almacenado
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(ITERATIONS)
                .withMemoryAsKB(MEMORY)
                .withParallelism(PARALLELISM);

        // Generar hash para comparar
        byte[] testHash = new byte[HASH_LENGTH];
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        generator.generateBytes(rawPassword.toString().toCharArray(), testHash);

        // Comparar los hashes
        return Arrays.equals(hash, testHash);
    }
}
