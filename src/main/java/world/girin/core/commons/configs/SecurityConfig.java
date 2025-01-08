package world.girin.core.commons.configs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import world.girin.core.commons.users.jwt.CustomLogoutFilter;
import world.girin.core.commons.users.jwt.JwtFilter;
import world.girin.core.commons.users.jwt.JwtUtil;
import world.girin.core.commons.users.jwt.RefreshTokenRepository;
import world.girin.core.commons.users.oauth2.CustomOAuth2UserService;
import world.girin.core.commons.users.oauth2.CustomSuccessHandler;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomSuccessHandler customSuccessHandler;

    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          CustomSuccessHandler customSuccessHandler,
                          JwtUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RefreshTokenRepository refreshTokenRepository) throws Exception {

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(1800L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        http
                .addFilterAfter(new JwtFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customSuccessHandler)
                );

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/index.html", "/healthcheck").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().permitAll());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
