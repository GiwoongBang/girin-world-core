package world.girin.core.commons.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {

        return "Girin Private Server | Contact: bku6713@gmail.com";
    }

    @GetMapping("/healthcheck")
    public String healthcheck() {

        return "ok";
    }

}
