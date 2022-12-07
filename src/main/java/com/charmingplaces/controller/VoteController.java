package com.charmingplaces.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charmingplaces.entity.Place;
import com.charmingplaces.entity.Vote;
import com.charmingplaces.pojo.PlacesDto;
import com.charmingplaces.pojo.VoteRequestDto;
import com.charmingplaces.service.PlaceService;
import com.charmingplaces.service.VoteService;

/**
 * Esta clase se encarga de gestionar peticiones referentes a los votos de los
 * usuarios.
 * 
 * Esta clase contiene dos endpoint, el de crear votos, y el de eliminarlos.
 * 
 * @author Ana María Ramírez Dorado
 *
 */
@RestController
@RequestMapping("/votes")
public class VoteController {

	private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);

	@Autowired
	private VoteService voteService;
	
	@Autowired
	private PlaceService placeService;


	/**
	 * Añade un voto del lugar de interés. Busca si un usuario ha dado un like, si
	 * es así, lo añade en el listado de lugares que el usuario ha votado. Además de
	 * esto, busca el lugar que ha sido votado y su cantidad de votos, y lo
	 * actualiza retornando los datos del lugar actualizado.
	 * 
	 * @param voteRequestDto contiene la id de usuario y la id de lugar
	 * @return datos del lugar actualizado
	 */
	@PostMapping("/upVote")
	public ResponseEntity<PlacesDto> upVote(@RequestBody VoteRequestDto voteRequestDto) {

		String placeId = voteRequestDto.getPlaceId();
		String userId = voteRequestDto.getUserId();
		
		Vote vote = voteService.findById(userId);

		// Create or update vote
		if (vote == null) {
			vote = new Vote();
			vote.setUserId(userId);
		}

		vote.getPlaces().add(placeId);
		voteService.save(vote);
		
		Place existingPlace = updatePlaceVotes(placeId);

		PlacesDto result = buildResponse(existingPlace, userId);
		return ResponseEntity.ok(result );
	}

	/**
	 * Elimina un voto del lugar de interés. Busca si un usuario ha dado un like, si
	 * es así, lo elimina del listado de lugares que el usuario ha votado. Además de
	 * esto, busca el lugar que ha sido desvotado y su cantidad de votos, y lo
	 * actualiza retornando los datos del lugar actualizado.
	 * 
	 * @param voteRequestDto contiene la id de usuario y la id de lugar
	 * @return datos del lugar actualizado
	 */
	@PostMapping("/downVote")
	public ResponseEntity<PlacesDto> downVote(@RequestBody VoteRequestDto voteRequestDto) {
		String placeId = voteRequestDto.getPlaceId();
		String userId = voteRequestDto.getUserId();

		Vote vote = voteService.findById(userId);

		// Create or update vote
		if (vote == null) {
			vote = new Vote();
			vote.setUserId(userId);
		}
		vote.getPlaces().remove(placeId);
		voteService.save(vote);

		Place existingPlace = updatePlaceVotes(placeId);

		PlacesDto result = buildResponse(existingPlace, userId);
		return ResponseEntity.ok(result);
	}


	/**
	 * Actualiza los votos del lugar
	 * 
	 * @param placeId id del lugar
	 * @return lugar actualizado
	 */
	private Place updatePlaceVotes(String placeId) {
		List<Vote> votesFromPlace = voteService.findByPlaceId(placeId);
		
		Place existingPlace = placeService.findById(placeId);
		existingPlace.setVotes(votesFromPlace.size());
		placeService.updatePlaceVotes(existingPlace);
		return existingPlace;
	}

	/**
	 * Construye la respuesta que se mandará de nuevo al front, con los datos del
	 * lugar actualziados
	 * 
	 * @param existingPlace lugar votado y actualizado
	 * @param userId        id del usuario que ha emitido el voto
	 * @return lugar con los datos actualizados
	 */
	private PlacesDto buildResponse(Place existingPlace, String userId) {
		Vote voteUser = voteService.findByUserIdAndPlaces(userId, existingPlace.getId());
		return PlacesDto.builder()
				.id(existingPlace.getId())
				.name(existingPlace.getName())
				.votes(existingPlace.getVotes())
				.voted(voteUser != null)
				.build();
	}
}

