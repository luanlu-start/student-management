package vn.edu.fpt.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.repository.UserRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();

    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            String role = user.getRole() == null ? "" : user.getRole().trim().toLowerCase().replace(' ', '_');
            if ("teacher".equals(role)) {
                role = "lecturer"; // Backward-compat with existing data
            }

            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
            );
        };
    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig
//    ) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }

    // FilterChain (lọc request)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                            "/login", "/logout", "/error", "/css/**", "/js/**", "/assets/**", "/static/**"
                        ).permitAll() // permitAll = cho phép công cộng
                        .requestMatchers("/", "/home").authenticated() // Trang chủ cho tất cả role đã xác thực
                        .anyRequest().authenticated() // anyReq = tất cả các request còn lại, buộc phải xác thực
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .successHandler((request, response, authentication) -> {
                            userRepository.findByUsername(authentication.getName())
                                    .ifPresent(user -> request.getSession().setAttribute("user", user));
                            response.sendRedirect("/home");
                        })
                        .failureUrl("/login?message=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .permitAll()
                )
                .build();


    }
}
