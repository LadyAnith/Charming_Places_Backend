package com.charmingplaces.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charmingplaces.entity.Photo;
import com.charmingplaces.entity.Response;

@RestController
@RequestMapping("/lugares")
public class PlaceController {
	
	@GetMapping("/")
	public String testing() {
		return "Probando, probando 1, 2 y 3!!";
	}

	@PostMapping(value = "/img")
	public Response addPhoto(@RequestBody Photo p) throws IOException {
		
		Path path = Paths.get("/users/Anith/desktop/imagen.jpg");
		Files.write(path, p.getImage());
		
		return new Response();
	}
}
