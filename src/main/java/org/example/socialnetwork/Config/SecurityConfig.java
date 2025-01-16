package org.example.socialnetwork.Config;

import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/users/register", "/users/login").permitAll() // Открытые страницы
                        .requestMatchers("/messages", "/users/profile").authenticated() // Доступ только для аутентифицированных пользователей
                       // .requestMatchers("/admin/**").hasRole("ADMIN") // Доступ для администраторов
                        .anyRequest().authenticated() // Все остальные страницы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/users/login") // Страница входа
                        .permitAll() // Разрешить всем доступ к странице входа
                )
                .logout(logout -> logout
                        .permitAll() // Разрешить всем выходить из системы
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Устанавливаем UserDetailsServiceImpl для аутентификации
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}
