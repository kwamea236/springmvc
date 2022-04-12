package com.web.springbootmvc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.annotation.NonNull;


@SpringBootApplication
public class SpringbootmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootmvcApplication.class, args);
	}

}

@RequiredArgsConstructor
@Controller
class PositionController {
	@NonNull
	private final AircraftRepository aircraftRepository;
	private WebClient client = WebClient.create("http://localhost:7634/aircraft");

	@GetMapping("/aircraft")
	public String getCurrentAircraftPosition(Model model){
		aircraftRepository.deleteAll();

		client.get()
				.retrieve()
				.bodyToFlux(Aircraft.class)
				.filter(plane -> !plane.getReg().isEmpty())
				.toStream()
				.forEach(aircraftRepository::save);

		model.addAttribute("currentPosition", aircraftRepository.findAll());
		return "positions";
	}
}