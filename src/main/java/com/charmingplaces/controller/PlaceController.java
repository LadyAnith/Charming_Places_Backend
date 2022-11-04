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

import com.charmingplaces.entity.Photo;
import com.charmingplaces.entity.Place;
import com.charmingplaces.entity.Response;
import com.charmingplaces.service.PlaceService;

@RestController
@RequestMapping("/lugares")
public class PlaceController {
	
	@Autowired
	private PlaceService service;
	
	@GetMapping("/")
	public String testing() {
		return "Probando, probando si pilla los cambios tras desplegar... 1, 2 y 3!!";
	}

	@PostMapping(value = "/img")
	public Response addPhoto(@RequestBody Photo p) throws IOException {
		
		Path path = Paths.get("/users/Anith/desktop/imagen.jpg");
		Files.write(path, p.getImage());
		
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
