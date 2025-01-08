package world.girin.core.commons.users.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import world.girin.core.commons.users.jwt.JwtUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2UserEntity customUserDetails = (CustomOAuth2UserEntity) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String role = getRole(authentication);
        String nickname = customUserDetails.getName();
        String profileImg = customUserDetails.getProfileImg();

        String accessToken = jwtUtil.generateAccessToken(username, "access", role, nickname, profileImg);
        String refreshToken = jwtUtil.generateRefreshToken(username, "refresh", role, nickname, profileImg);

        Date refreshTokenExpiration = jwtUtil.getExpiration(refreshToken);
        jwtUtil.saveRefreshToken(username, refreshToken, refreshTokenExpiration);

        response.addCookie(jwtUtil.createCookie("Authorization", "BEARER_" + accessToken));
        response.addCookie(jwtUtil.createCookie("Refresh-Token", "BEARER_" + refreshToken));
        response.sendRedirect("https://e6fe-118-176-146-86.ngrok-free.app");
    }

    private static String getRole(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        return role;
    }

}
