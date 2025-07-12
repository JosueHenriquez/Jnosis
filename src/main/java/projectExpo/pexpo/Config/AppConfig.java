package projectExpo.pexpo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import projectExpo.pexpo.Utils.JWTUtils;
import projectExpo.pexpo.Utils.JwtCookieAuthFilter;

@Configuration
public class AppConfig {

    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils){

        return new JwtCookieAuthFilter(jwtUtils);
    }
}
