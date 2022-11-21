package com.charmingplaces.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Place;
import com.charmingplaces.pojo.PlacesNearRequestDto;
import com.charmingplaces.pojo.PlacesNearResponseDto;
import com.charmingplaces.pojo.PlacesNearResponseDto.PlacesDto;
import com.charmingplaces.repository.PlaceRepository;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

@Service
public class PlaceServiceImpl implements PlaceService{
	

	@Autowired
	PlaceRepository repo;
	
	@Autowired
	MongoTemplate mongoTemplate;

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

	@Override
	public PlacesNearResponseDto findNear(PlacesNearRequestDto placesNearRequestDto) {
		
		String query = "{location: {$near: {$maxDistance: %s, $geometry: {type: \"%s\", coordinates: [%s]}}}}";
		String coordinates = String.format("%s,%s", placesNearRequestDto.getXcoord(),placesNearRequestDto.getYcoord() );
		String queryFormat = String.format(query, 1000, "Point", coordinates);
		
		BasicQuery bquery = new BasicQuery(queryFormat);
		
		List<Place> data = mongoTemplate.find(bquery, Place.class);
		
		PlacesNearResponseDto result = new PlacesNearResponseDto();
		for (Place place : data) {
			PlacesDto p = new PlacesDto();
			p.setId(place.getId());
			p.setName(place.getName());
			p.setXcoord(place.getLocation().getCoordinates().get(0));
			p.setYcoord(place.getLocation().getCoordinates().get(1));
			result.getData().add(p);
		}
		
		return result;
	}

}
