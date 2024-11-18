package com.vsii.coursemanagement.configurations;

import com.vsii.coursemanagement.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Danh sách các URL được phép truy cập mà không cần xác thực, bao gồm các endpoint của Swagger.
     */
    public String[] getWhiteListUrl() {
        return new String[] {
                String.format("%s/accounts/**", apiPrefix),  // Các endpoint không cần xác thực
                "/v2/api-docs", "/v3/api-docs/**",           // Swagger docs
                "/swagger-resources/**",                     // Swagger resources
                "/swagger-ui/**", "/webjars/**"              // Swagger UI
        };
    }

    /**
     * Cấu hình chuỗi lọc bảo mật.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .authorizeRequests(auth -> auth
                        .requestMatchers(getWhiteListUrl()).permitAll()


                        .requestMatchers(HttpMethod.GET, apiPrefix + "/courses/**").hasAnyAuthority("admin:retrieve", "user:retrieve")
                        .requestMatchers(HttpMethod.POST, apiPrefix + "/courses/**").hasAuthority("admin:create")
                        .requestMatchers(HttpMethod.PUT, apiPrefix + "/courses/**").hasAuthority("admin:update")
                        .requestMatchers(HttpMethod.DELETE, apiPrefix + "/courses/**").hasAuthority("admin:delete")

                        .requestMatchers(HttpMethod.GET, apiPrefix + "/categories/**").hasAnyAuthority("admin:retrieve", "user:retrieve")

                        .requestMatchers(HttpMethod.GET, apiPrefix + "/languages/**").hasAnyAuthority("admin:retrieve", "user:retrieve")

                        .requestMatchers(HttpMethod.GET, apiPrefix + "/instructors/**").hasAnyAuthority("admin:retrieve", "user:retrieve")

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Cấu hình CORS.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Auth-Token"));
        configuration.setExposedHeaders(List.of("X-Auth-Token"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
