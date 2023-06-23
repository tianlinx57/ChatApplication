package fr.utc.sr03.chat.config;

import fr.utc.sr03.chat.config.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // L'intercepteur est enregistré dans le conteneur
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // Toutes les requêtes sont bloquées, sans les ressources suivantes
                .excludePathPatterns("/", "/css/**", "/fonts/**", "/images/**",
                        "/js/**","/login","/logout","/api/**","/websocket/**");
    }
}
