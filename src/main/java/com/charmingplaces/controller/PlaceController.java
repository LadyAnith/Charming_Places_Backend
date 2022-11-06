package com.charmingplaces.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.charmingplaces.entity.Place;
import com.charmingplaces.entity.Response;
import com.charmingplaces.pojo.PhotoDto;
import com.charmingplaces.service.ImageUploadService;
import com.charmingplaces.service.PlaceService;

@RestController
@RequestMapping("/lugares")
public class PlaceController {
	
	@Autowired
	private PlaceService service;

	@Autowired
	private ImageUploadService imageUploadService;
	
	@GetMapping("/")
	public String testing() {
		return "Probando, probando... 1, 2 y 3!!";
	}

	@PostMapping(value = "/img")
	public Response addPhoto(@RequestBody PhotoDto p) throws IOException {
		
		Path path = Paths.get("/users/Anith/desktop/imagen.jpg");
		Files.write(path, p.getImage());
		
		return new Response();
	}
	
	//Se va a usar este endpoint para simular desde postman que mandamos la info como si fuese android
	@PostMapping(value = "/imgSimulaAndroid")
	public Response addPhotoSimulaAndroid(@RequestBody PhotoDto p) throws IOException {
		
		imageUploadService.uploadImage(p);
		
		return new Response();
	}
	
	
	@PostMapping(value = "/add")
	public ResponseEntity<?> add(@RequestBody Place place) {
		
		Place result = this.service.save(place);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping(value = "/all")
	public Collection<Place> readAll() {
		return service.findAll();
	}
	
	
	@GetMapping(value = "/{id}")
	public Place findById(@PathVariable String id) {
		return service.findById(id);
	}
	
	
		@PutMapping("/{id}")
		public Place updatePerfil(@PathVariable String id, @RequestBody Place place) {

			Place result = this.service.update(place).orElseThrow(null);
			return result;
		}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable String id) {
		service.deleteById(id);
	}
	
	
	
}
