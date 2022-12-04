package com.charmingplaces.service;

import java.util.Optional;

import com.charmingplaces.entity.Place;
import com.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.charmingplaces.pojo.PlacesListResponseDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;


public interface PlaceService {
	public PlacesListResponseDto findAll(String userId);

	public Place findById(String id);

	public PlacesListResponseDto findNear(PlacesNearRequestDto placesNearRequestDto, String userId);

	public PlacesListResponseDto findPlacesInsideArea(PlacesInsideAreaRequestDto request, String userId);

	public Place save(Place place);

	public void deleteById(String id);

	public Optional<Place> update(Place place);


}
