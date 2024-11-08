package world.grin.core.common.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "ok";
    }
}
