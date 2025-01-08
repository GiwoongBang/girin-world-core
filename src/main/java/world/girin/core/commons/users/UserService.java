package world.girin.core.commons.users;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import world.girin.core.commons.users.jwt.JwtUtil;
import world.girin.core.commons.users.jwt.RefreshTokenRepository;

import java.util.Date;

@Service
public class UserService {

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    public UserService(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Refresh-Token")) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }

        if (authorization == null | !authorization.startsWith("BEARER_")) {

            return new ResponseEntity<>("Refresh Token is NULL", HttpStatus.BAD_REQUEST);
        }

        String refreshToken = authorization.substring(7);
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            return new ResponseEntity<>("Refresh Token is expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (category == null || !category.equals("refresh")) {

            return new ResponseEntity<>("Invalid Refresh Token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) return new ResponseEntity<>("Invalid Refresh Token", HttpStatus.BAD_REQUEST);

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String nickname = jwtUtil.getNickname(refreshToken);
        String profileImg = jwtUtil.getProfileImg(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(username, "access", role, nickname, profileImg);
        String newRefreshToken = jwtUtil.generateRefreshToken(username, "refresh", role, nickname, profileImg);

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        Date newRefreshTokenExpiration = jwtUtil.getExpiration(newRefreshToken);
        jwtUtil.saveRefreshToken(username, newRefreshToken, newRefreshTokenExpiration);

        response.addCookie(jwtUtil.createCookie("Authorization", "BEARER_" + newAccessToken));
        response.addCookie(jwtUtil.createCookie("Refresh-Token", "BEARER_" + newRefreshToken));

        return new ResponseEntity<>("CLEAR", HttpStatus.OK);
    }

}
