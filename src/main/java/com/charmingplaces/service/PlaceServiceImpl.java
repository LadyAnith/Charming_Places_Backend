package com.charmingplaces.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Place;
import com.charmingplaces.repository.PlaceRepository;

@Service
public class PlaceServiceImpl implements PlaceService{
	

	@Autowired
	PlaceRepository repo;

	@Override
	public List<Place> findAll() {
		return repo.findAll();
	}

	@Override
	public Place findById(String id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Place save(Place place) {
		return repo.save(place);
	}

	@Override
	public void deleteById(String id) {
		repo.deleteById(id);
		
	}

	@Override
	public Optional<Place> update(Place place) {
		
		Place oldPlace = repo.findById(place.getId()).orElse(null);

		Optional<Place> result = Optional.ofNullable(oldPlace);

		return result;
	}

}
