package com.charmingplaces.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Image;
import com.charmingplaces.entity.Place;
import com.charmingplaces.pojo.PlacesDto;
import com.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.charmingplaces.pojo.PlacesListResponseDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;
import com.charmingplaces.repository.PlaceRepository;

@Service
public class PlaceServiceImpl implements PlaceService {

	private static final Logger LOG = LoggerFactory.getLogger(PlaceServiceImpl.class);

	@Autowired
	ImageService imageService;

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

		return Optional.ofNullable(oldPlace);
	}

	@Override
	public PlacesListResponseDto findNear(PlacesNearRequestDto placesNearRequestDto) {

		String query = "{location: {$near: {$maxDistance: %s, $geometry: {type: \"Point\", coordinates: [%s]}}}}";
		String coordinates = String.format("%s,%s", placesNearRequestDto.getXcoord(), placesNearRequestDto.getYcoord());
		String queryFormat = String.format(query, 1000, coordinates);

		Query bquery = new BasicQuery(queryFormat).limit(100);
		List<Place> data = mongoTemplate.find(bquery, Place.class);

		PlacesListResponseDto result = placesToplacesDto(data);

		LOG.info("lugares encontrados {} : {}", result.getData().size(), result.getData());
		return result;
	}

	@Override
	public PlacesListResponseDto findPlacesInsideArea(PlacesInsideAreaRequestDto request) {

		String query = "{location: {$geoWithin: {$box: [%s]}}}";

		String listCoordinates = getBoxPoints(request);
		String queryFormat = String.format(query, listCoordinates);

		Query bquery = new BasicQuery(queryFormat).limit(100);
		List<Place> data = mongoTemplate.find(bquery, Place.class);

		PlacesListResponseDto result = placesToplacesDto(data);

		LOG.info("lugares encontrados {} : {}", result.getData().size(), result.getData());
		return result;
	}

	private String getBoxPoints(PlacesInsideAreaRequestDto request) {
		StringBuilder sb = new StringBuilder();

		sb.append("[");
		sb.append(request.getGeoPointTopLeft().getXcoord());
		sb.append(",");
		sb.append(request.getGeoPointTopLeft().getYcoord());
		sb.append("], [");
		sb.append(request.getGeoPointBottomRight().getXcoord());
		sb.append(",");
		sb.append(request.getGeoPointBottomRight().getYcoord());
		sb.append("]");

		return sb.toString();

	}

	private PlacesListResponseDto placesToplacesDto(List<Place> data) {
		PlacesListResponseDto result = new PlacesListResponseDto();
		for (Place place : data) {
			PlacesDto p = new PlacesDto();
			p.setId(place.getId());
			p.setName(place.getName());
			p.setXcoord(place.getLocation().getCoordinates().get(0));
			p.setYcoord(place.getLocation().getCoordinates().get(1));

			Image imageData = imageService.findByImageId(place.getImageId());
			p.setUrl(imageToBase64(imageData.getLink()));

			result.getData().add(p);
		}
		return result;
	}

	private String imageToBase64(String imageurl) {

		try {
			URL url = new URL(imageurl);
			BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
			byte[] sourceBytes = IOUtils.toByteArray(bis);
			return Base64.getEncoder().encodeToString(sourceBytes);
		} catch (IOException e) {
			return "";
		}

	}
}
