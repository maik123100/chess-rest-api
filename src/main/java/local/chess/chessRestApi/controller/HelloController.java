package local.chess.chessRestApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

	@GetMapping("/hello")
	public Map<String, String> sayHello() {
		return Map.of(
				"message", "Hello, Chess API is running!",
				"env", "hi");
	}

	@GetMapping("/passwords")
	public Map<String, String> sayPasswords() {
		return Map.of(
				"You Piece", "Don't get Passwords");
	}
}
