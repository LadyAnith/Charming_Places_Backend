package com.charmingplaces.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Vote;
import com.charmingplaces.repository.VoteRepository;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	VoteRepository voteRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Vote findByUserIdAndPlace(String userId, String placeId) {
		return voteRepository.findByUserIdAndPlaces(userId, placeId).orElse(null);
	}

	@Override
	public List<Vote> findByPlaceId(String placeId) {

		return voteRepository.findByPlaces(placeId);
	}

	@Override
	public Vote save(Vote vote) {
		return voteRepository.save(vote);
	}

	@Override
	public void deleteById(String id) {
		voteRepository.deleteById(id);

	}

	@Override
	public Optional<Vote> update(Vote vote) {

		return Optional.ofNullable(voteRepository.save(vote));
	}
}
