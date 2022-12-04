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
import com.charmingplaces.repository.VoteRepository;
import com.charmingplaces.service.PlaceService;
import com.charmingplaces.service.VoteService;

@RestController
@RequestMapping("/votes")
public class VoteController {

	private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);

	@Autowired
	private VoteRepository voteDao;
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private PlaceService placeService;


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


	private Place updatePlaceVotes(String placeId) {
		List<Vote> votesFromPlace = voteService.findByPlaceId(placeId);
		
		Place existingPlace = placeService.findById(placeId);
		existingPlace.setVotes(votesFromPlace.size());
		placeService.update(existingPlace);
		return existingPlace;
	}

	private PlacesDto buildResponse(Place existingPlace, String userId) {
		Vote voteUser = voteService.findByUserIdAndPlace(userId, existingPlace.getId());
		return PlacesDto.builder()
				.id(existingPlace.getId())
				.name(existingPlace.getName())
				.votes(existingPlace.getVotes())
				.voted(voteUser != null)
				.build();
	}
}

