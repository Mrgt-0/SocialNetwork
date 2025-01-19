package org.example.socialnetwork.Config;

import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsServiceImpl userDetailsService() { return new UserDetailsServiceImpl(); }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/auth/register", "/auth/login").permitAll() // Открытые страницы
                        .requestMatchers("/messages", "/selectRecipient", "/send-message", "/users/profile", "/posts/**", "/friends/**", "/communities/**").authenticated() // Доступ только для аутентифицированных пользователей
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Доступ для администраторов
                        .anyRequest().authenticated() // Все остальные страницы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/users/profile", true)
                        .failureUrl("/auth/login?error=true")// Страница входа
                        .permitAll() // Разрешить всем доступ к странице входа
                )
                .logout(LogoutConfigurer::permitAll // Разрешить всем выходить из системы
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}
