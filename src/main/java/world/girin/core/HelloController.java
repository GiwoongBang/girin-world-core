package world.girin.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {

        return "Girin Private Server | Contact: bku6713@gamil.com";
    }

    @GetMapping("/healthcheck")
    public String healthcheck() {

        return "ok";
    }

}