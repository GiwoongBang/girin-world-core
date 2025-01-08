package world.girin.core.commons.users.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import world.girin.core.commons.users.dto.UserDto;
import world.girin.core.commons.users.oauth2.CustomOAuth2UserEntity;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }

        if (authorization == null || !authorization.startsWith("BEARER_")) {
            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = authorization.substring(7);
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            filterChain.doFilter(request, response);

            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (category == null || !category.equals("access")) {
            filterChain.doFilter(request, response);

            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        String nickname = jwtUtil.getNickname(accessToken);
        String profileImg = jwtUtil.getProfileImg(accessToken);

        UserDto userDto = UserDto.builder()
                .username(username)
                .role(role)
                .nickname(nickname)
                .profileImg(profileImg)
                .build();

        CustomOAuth2UserEntity customOAuth2User = new CustomOAuth2UserEntity(userDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}