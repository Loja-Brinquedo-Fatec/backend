package api.toystory.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Permite CORS em todos os endpoints
                .allowedOrigins("*") // Permite todas as origens
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite métodos HTTP
                .allowedHeaders("*"); // Permite todos os headers
    }
}