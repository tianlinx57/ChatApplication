package fr.utc.sr03.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/websocket/**").permitAll() // Autoriser les requêtes WebSocket
                .antMatchers("/login").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/forgot-password").permitAll()
                .antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/error", "/webjars/**").permitAll()
                .antMatchers("/reset-password*").permitAll()
                .anyRequest().authenticated() // 其他路径需要进行身份验证
                .and()
                .formLogin()
                .loginPage("/login") // 配置登录页面的URL
                .defaultSuccessUrl("/admin/accueil", true) // 配置登录成功后的重定向URL
                .and()
                .logout()
                .logoutUrl("/logout") // 配置注销URL
                .logoutSuccessUrl("/login") // 配置注销成功后的重定向URL
                .and()
                .csrf().disable(); // 禁用CSRF保护（仅供示例，实际应用中需要启用CSRF保护）
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        // 允许来自localhost:3000的跨源请求
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

