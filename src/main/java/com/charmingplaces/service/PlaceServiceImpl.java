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
	PlaceRepository placeRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	VoteService voteService;

	/**
	 * {@link PlaceService#findAll()}
	 */
	@Override
	public PlacesListResponseDto findAll(String userId) {

		Sort sort = Sort.by(Direction.DESC, "votes");
		PlacesListResponseDto result = placesToPlacesListResponseDto(placeRepository.findAll(sort), userId);

		LOG.info(LUGARES_ENCONTRADOS, result.getData().size(), result.getData());
		return result;

	}

	/**
	 * {@link PlaceService#findById()}
	 */
	@Override
	public Place findById(String id) {
		return placeRepository.findById(id).orElse(null);
	}

	/**
	 * {@link PlaceService#findFavorites()}
	 */
	@Override
	public PlacesListResponseDto findFavorites(String userId) {

		Vote vote = voteService.findById(userId);
		if (vote == null) {
			return new PlacesListResponseDto();
		}

		Set<String> favoritePlaceIds = vote.getPlaces();
		Iterable<Place> iterable = placeRepository.findAllById(favoritePlaceIds);
		List<Place> placeList = IterableUtils.toList(iterable);
		return placesToPlacesListResponseDto(placeList, userId);
	}

	/**
	 * {@link PlaceService#findNear()}
	 */
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

	/**
	 * {@link PlaceService#findPlacesInsideArea()}
	 */
	@Override
	public PlacesListResponseDto findPlacesInsideArea(PlacesInsideAreaRequestDto request, String userId) {

		String query = "{location: {$geoWithin: {$box: [%s]}}}";

		String listCoordinates = getAreaPoints(request);
		String queryFormat = String.format(query, listCoordinates);

		Query bquery = new BasicQuery(queryFormat).limit(100);
		List<Place> data = mongoTemplate.find(bquery, Place.class);


		PlacesListResponseDto result = placesToPlacesListResponseDto(data, userId);

		LOG.info(LUGARES_ENCONTRADOS, result.getData().size(), result.getData());
		return result;
	}

	/**
	 * {@link PlaceService#save()}
	 */
	@Override
	public Place save(Place place) {
		return placeRepository.save(place);
	}

	/**
	 * {@link PlaceService#updatePlaceVotes()}
	 */
	@Override
	public Place updatePlaceVotes(Place place) {

		Optional<Place> currentPlace = placeRepository.findById(place.getId());
		if (currentPlace.isPresent()) {
			Place placePersist = currentPlace.get();
			placePersist.setVotes(place.getVotes());

			return placeRepository.save(placePersist);
		}

		return null;
	}

// METODOS AUXILIARES

	/**
	 * transforma las coordenadas superior izquierda e inferior derecha en un string
	 * con el formato de box para usar con mongo ej:
	 * [TOP_LEFT_xcoord,TOP_LEFT_ycoord],[BOTTOM_RIGHT_xcoord _BOTTOM_RIFHT_ycoord]
	 * 
	 * @param request puntos del area
	 * @return coordenadas transformadas a string
	 */
	private String getAreaPoints(PlacesInsideAreaRequestDto request) {
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

	/**
	 * Este método convierte un listado de Place en PlacesDto.
	 * 
	 * Por cada elemento de la lista setea si lo ha votado el usuario.
	 * 
	 * Busca los datos de la imagen para setear el contenido de la imagen en Base64
	 * utilizando el enlace de imgurl.
	 * 
	 * y finalmente empaqueta todos los elementos en el objeto PlacesListResponseDto
	 * 
	 * @param data   listado de lugares
	 * @param userId id del usuario
	 * @return el PlacesListResponseDto que contiene el listado de PlacesDto
	 */
	private PlacesListResponseDto placesToPlacesListResponseDto(List<Place> data, String userId) {
		PlacesListResponseDto result = new PlacesListResponseDto();
		for (Place place : data) {
			PlacesDto placeDto = new PlacesDto();
			placeDto.setId(place.getId());
			placeDto.setName(place.getName());
			placeDto.setXcoord(place.getLocation().getCoordinates().get(0));
			placeDto.setYcoord(place.getLocation().getCoordinates().get(1));
			placeDto.setVotes(place.getVotes());

			Vote voteUser = voteService.findByUserIdAndPlaces(userId, placeDto.getId());
			placeDto.setVoted(voteUser != null);

			Image imageData = imageService.findByImageId(place.getImageId());
			placeDto.setImageContent(imageToBase64(imageData.getLink()));

			result.getData().add(placeDto);
		}
		return result;
	}

	/**
	 * Método encargado de extraer la imagen almacenada en Imgur, usando su enlace
	 * público y convertirla en Base64
	 * 
	 * @param imageLink enlace público de imgur
	 * @return imagen en el formato Base64
	 */
	private String imageToBase64(String imageLink) {

		try {
			URL url = new URL(imageLink);
			BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
			byte[] sourceBytes = IOUtils.toByteArray(bis);
			return Base64.getEncoder().encodeToString(sourceBytes);
		} catch (IOException e) {
			return "";
		}

	}


}
