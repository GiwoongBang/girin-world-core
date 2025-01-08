package world.girin.core.commons.apis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import world.girin.core.commons.users.UserService;
import world.girin.core.commons.users.jwt.JwtUtil;

@RestController
public class UserController {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/users/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {

        return userService.reissueToken(request, response);
    }

}