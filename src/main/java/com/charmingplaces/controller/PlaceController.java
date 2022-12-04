package com.charmingplaces.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charmingplaces.pojo.CreatePlaceRequestDto;
import com.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.charmingplaces.pojo.PlacesListResponseDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;
import com.charmingplaces.service.ImageUploadService;
import com.charmingplaces.service.PlaceService;

@RestController
@RequestMapping("/places")
public class PlaceController {

	private static final Logger LOG = LoggerFactory.getLogger(PlaceController.class);

	private static final String USER_ID = "user-id";

	@Autowired
	private PlaceService service;

	@Autowired
	private ImageUploadService imageUploadService;
	
	@GetMapping
	public ResponseEntity<PlacesListResponseDto> findAll(@RequestHeader Map<String, String> headers) {
		String userId = headers.get(USER_ID);
		return ResponseEntity.ok(service.findAll(userId));
	}

	@GetMapping("/findFavorites")
	public ResponseEntity<PlacesListResponseDto> findFavorites(@RequestHeader Map<String, String> headers) {
		String userId = headers.get(USER_ID);
		return ResponseEntity.ok(service.findFavorites(userId));
	}

	@GetMapping("/findNear")
	public ResponseEntity<PlacesListResponseDto> findNear(@RequestHeader Map<String, String> headers,
			PlacesNearRequestDto placesNearRequestDto) {
		String userId = headers.get(USER_ID);

		PlacesListResponseDto result = service.findNear(placesNearRequestDto, userId);
		
		return ResponseEntity.ok(result);

	}
	
	@PostMapping
	public ResponseEntity<CreatePlaceRequestDto> create(@RequestHeader Map<String, String> headers, @RequestBody CreatePlaceRequestDto request) {
		LOG.info("ENTRANDO en addPhoto request : {}", request);

		
		String userId = headers.get(USER_ID);
		CreatePlaceRequestDto result = imageUploadService.uploadImage(userId, request);

		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/placesInsideArea")
	public ResponseEntity<PlacesListResponseDto> findPlacesInsideArea(@RequestHeader Map<String, String> headers,
			@RequestBody PlacesInsideAreaRequestDto request) {
		String userId = headers.get(USER_ID);
		LOG.info("ENTRANDO en findPlacesInsideArea request : {}", request);
		PlacesListResponseDto result = service.findPlacesInsideArea(request, userId);
		return ResponseEntity.ok(result);
	}



}
