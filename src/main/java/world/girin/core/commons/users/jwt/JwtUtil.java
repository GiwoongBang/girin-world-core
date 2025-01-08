package world.girin.core.commons.users.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.jwt.access.expiration}")
    private int JWT_ACCESS_EXPIRATION_TIME;

    @Value("${spring.jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION_TIME;

    private final SecretKey secretKey;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret,
                   RefreshTokenRepository refreshTokenRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getNickname(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getProfileImg(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("profileImg", String.class);
    }

    public Date getExpiration(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String generateAccessToken(String username, String category, String role, String nickname, String profileImg) {

        return Jwts.builder()
                .claim("username", username)
                .claim("category", category)
                .claim("role", role)
                .claim("nickname", nickname)
                .claim("profileImg", profileImg)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_ACCESS_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username, String category, String role, String nickname, String profileImg) {

        return Jwts.builder()
                .claim("username", username)
                .claim("category", category)
                .claim("role", role)
                .claim("nickname", nickname)
                .claim("profileImg", profileImg)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);

        if (key.equals("Authorization")) {
            cookie.setMaxAge(JWT_ACCESS_EXPIRATION_TIME / 1000);
        } else {
            cookie.setMaxAge(JWT_REFRESH_EXPIRATION_TIME / 1000);
        }
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public void saveRefreshToken(String username, String refreshToken, Date expiration) {

        Boolean isExist = refreshTokenRepository.existsByUsername(username);
        if(isExist) refreshTokenRepository.deleteAllByusername(username);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .username(username)
                .refreshToken(refreshToken)
                .expriration(expiration)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

}
