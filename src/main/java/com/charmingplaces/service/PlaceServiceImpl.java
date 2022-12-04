package com.charmingplaces.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Image;
import com.charmingplaces.entity.Place;
import com.charmingplaces.entity.Vote;
import com.charmingplaces.pojo.PlacesDto;
import com.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.charmingplaces.pojo.PlacesListResponseDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;
import com.charmingplaces.repository.PlaceRepository;

@Service
public class PlaceServiceImpl implements PlaceService {

	private static final String LUGARES_ENCONTRADOS = "lugares encontrados {} : {}";

	private static final Logger LOG = LoggerFactory.getLogger(PlaceServiceImpl.class);

	@Autowired
	ImageService imageService;

	@Autowired
	PlaceRepository repo;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	VoteService voteService;

	@Override
	public PlacesListResponseDto findAll(String userId) {

		Sort sort = Sort.by(Direction.DESC, "votes");
		PlacesListResponseDto result = placesToPlacesListResponseDto(repo.findAll(sort), userId);

		LOG.info(LUGARES_ENCONTRADOS, result.getData().size(), result.getData());
		return result;

	}

	@Override
	public Place findById(String id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public PlacesListResponseDto findFavorites(String userId) {

		Vote vote = voteService.findById(userId);
		if (vote == null) {
			return new PlacesListResponseDto();
		}

		Set<String> favoritePlaces = vote.getPlaces();
		Iterable<Place> iterable = repo.findAllById(favoritePlaces);
		List<Place> placeList = IterableUtils.toList(iterable);
		return placesToPlacesListResponseDto(placeList, userId);
	}

	@Override
	public PlacesListResponseDto findNear(PlacesNearRequestDto placesNearRequestDto, String userId) {

		String query = "{location: {$near: {$maxDistance: %s, $geometry: {type: \"Point\", coordinates: [%s]}}}}";
		String coordinates = String.format("%s,%s", placesNearRequestDto.getXcoord(), placesNearRequestDto.getYcoord());
		String queryFormat = String.format(query, 1000, coordinates);

		Query bquery = new BasicQuery(queryFormat).limit(100);
		List<Place> data = mongoTemplate.find(bquery, Place.class);

		PlacesListResponseDto result = placesToPlacesListResponseDto(data, userId);

		LOG.info(LUGARES_ENCONTRADOS, result.getData().size(), result.getData());
		return result;
	}

	@Override
	public PlacesListResponseDto findPlacesInsideArea(PlacesInsideAreaRequestDto request, String userId) {

		String query = "{location: {$geoWithin: {$box: [%s]}}}";

		String listCoordinates = getBoxPoints(request);
		String queryFormat = String.format(query, listCoordinates);

		Query bquery = new BasicQuery(queryFormat).limit(100);
		List<Place> data = mongoTemplate.find(bquery, Place.class);


		PlacesListResponseDto result = placesToPlacesListResponseDto(data, userId);

		LOG.info(LUGARES_ENCONTRADOS, result.getData().size(), result.getData());
		return result;
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

		Optional<Place> currentPlace = repo.findById(place.getId());
		if (currentPlace.isPresent()) {
			Place placePersist = currentPlace.get();
			placePersist.setVotes(place.getVotes());

			return Optional.ofNullable(repo.save(placePersist));
		}

		return Optional.empty();
	}

// METODOS AUXILIARES

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

	private PlacesListResponseDto placesToPlacesListResponseDto(List<Place> data, String userId) {
		PlacesListResponseDto result = new PlacesListResponseDto();
		for (Place place : data) {
			PlacesDto p = new PlacesDto();
			p.setId(place.getId());
			p.setName(place.getName());
			p.setXcoord(place.getLocation().getCoordinates().get(0));
			p.setYcoord(place.getLocation().getCoordinates().get(1));
			p.setVotes(place.getVotes());

			Vote voteUser = voteService.findByUserIdAndPlace(userId, p.getId());
			p.setVoted(voteUser != null);

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
