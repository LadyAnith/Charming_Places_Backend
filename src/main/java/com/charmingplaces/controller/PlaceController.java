package com.charmingplaces.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.charmingplaces.entity.Place;
import com.charmingplaces.pojo.CreatePlaceRequestDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;
import com.charmingplaces.pojo.PlacesNearResponseDto;
import com.charmingplaces.service.ImageService;
import com.charmingplaces.service.ImageUploadService;
import com.charmingplaces.service.PlaceService;

@RestController
@RequestMapping("/lugares")
public class PlaceController {

	@Autowired
	private PlaceService service;

	@Autowired
	private ImageUploadService imageUploadService;
	
	@Autowired
	private ImageService imageService;

	@GetMapping("/")
	public String testing() {
		return "Probando, probando... 1, 2 y 3!!";
	}


	@PostMapping
	public ResponseEntity<Place> create(@RequestBody Place place) {
		
		Place result = this.service.save(place);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
				.toUri();

		return ResponseEntity.created(location).body(result);
	}

	@GetMapping
	public ResponseEntity<List<Place>> readAll() {
		return ResponseEntity.ok(service.findAll()) ;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Place> findById(@PathVariable String id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Place> updatePerfil(@PathVariable String id, @RequestBody Place place) {

		Place result = this.service.update(place).orElseThrow(null);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable String id) {
		service.deleteById(id);
		return ResponseEntity.ok(true);
	}
	
	

	@GetMapping("/findNear")	
	public ResponseEntity<PlacesNearResponseDto> findNear(PlacesNearRequestDto placesNearRequestDto){
		
		PlacesNearResponseDto result = service.findNear(placesNearRequestDto);
		
		return ResponseEntity.ok(result);
 
	}

	@PostMapping(value = "/img")
	public ResponseEntity<CreatePlaceRequestDto> addPhoto(@RequestBody CreatePlaceRequestDto p) {

//		Path path = Paths.get("/users/Anith/desktop/imagen.jpg");
//		Files.write(path, p.getImage());

		CreatePlaceRequestDto result = imageUploadService.uploadImage(p);
		
		return ResponseEntity.ok(result);
	}

	// Se va a usar este endpoint para simular desde postman que mandamos la info
	// como si fuese android
	@PostMapping(value = "/imgSimulaAndroid")
	public ResponseEntity<CreatePlaceRequestDto> addPhotoSimulaAndroid(@RequestBody CreatePlaceRequestDto p) {

		CreatePlaceRequestDto result = imageUploadService.uploadImage(p);

		return ResponseEntity.ok(result);
	}

}
