package com.vsii.coursemanagement.configurations;

import com.vsii.coursemanagement.filters.JwtTokenFilter;
import com.vsii.coursemanagement.services.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;
    private final IPermissionService permissionService;

    /**
     * Danh sach các URL không cần phân quyền
     */
    public String[] getWhiteListUrl() {
        return new String[] {
                String.format("%saccounts/**", apiPrefix),
                String.format("%srsa/**", apiPrefix),
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**"
        };
    }

    /**
     * Cấu hình bảo mật
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        Map<String, Map<String, Set<String>>> permissionMappings = permissionService.loadPermissions();

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(getWhiteListUrl()).permitAll();

                    // config author for api
                    permissionMappings.forEach((endPoint, methodMap) ->
                            methodMap.forEach((httpMethod, permissions) -> {
//                                System.out.println("Endpoint: " + endPoint + " with methods: " + methodMap);
//                                System.out.println("Configuring method: " + httpMethod + " with permissions: " + permissions);

                                String fullUrl = apiPrefix + endPoint;
                                auth.requestMatchers(HttpMethod.valueOf(httpMethod), fullUrl)
                                        .hasAnyAuthority(permissions.toArray(new String[0]));
                            })
                    );

                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider); // Đảm bảo provider được sử dụng
        return http.build();
    }

    /**
     * Cấu hình CORS
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterForSecurity() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:5173");
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        CorsFilter corsFilter = new CorsFilter(request -> corsConfig);
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.addUrlPatterns("/" + apiPrefix + "*");

        return registrationBean;
    }

}
