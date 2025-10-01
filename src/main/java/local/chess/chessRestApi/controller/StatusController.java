package local.chess.chessRestApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class StatusController {

  @GetMapping("/helloworld")
  public Map<String, String> sayHello() {
    return Map.of(
        "message", "Hello, Chess API is running!");
  }
}
