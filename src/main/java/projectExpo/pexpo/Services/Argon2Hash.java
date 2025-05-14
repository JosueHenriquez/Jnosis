package projectExpo.pexpo.Services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.Getter;
import lombok.Setter;

public class Argon2Hash {
    public Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private static final int ITERATIONS = 1;
    private static final int MEMORY_COST = 1024;  // 64MB
    private static final int PARALLELISM = 1;

    @Getter @Setter
    private String password;

    public Argon2Hash() { }

    public String PasswordHash(){
        String hash = argon2.hash(ITERATIONS, MEMORY_COST, PARALLELISM, getPassword().toCharArray());
        System.out.println("1. Password: " + hash);
        return hash;
    }

    public boolean verifierHash(String passwordBase, String password){
        return argon2.verify(passwordBase, password);
    }
}
