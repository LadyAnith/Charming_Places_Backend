package com.charmingplaces.service;

import java.util.List;
import java.util.Optional;

import com.charmingplaces.entity.Place;


public interface PlaceService {
	public List<Place> findAll();

	public Place findById(String id);

	public Place save(Place place);

	public void deleteById(String id);

	public Optional<Place> update(Place place);

}
