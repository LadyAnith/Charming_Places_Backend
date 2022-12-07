package com.charmingplaces.service;

import java.util.List;

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

	/**
	 * {@link VoteService#findById()}
	 */
	@Override
	public Vote findById(String userId) {
		return voteRepository.findById(userId).orElse(null);
	}

	/**
	 * {@link VoteService#findByUserIdAndPlaces()}
	 */
	@Override
	public Vote findByUserIdAndPlaces(String userId, String placeId) {
		return voteRepository.findByUserIdAndPlaces(userId, placeId).orElse(null);
	}

	/**
	 * {@link VoteService#findByPlaceId()}
	 */
	@Override
	public List<Vote> findByPlaceId(String placeId) {

		return voteRepository.findByPlaces(placeId);
	}

	/**
	 * {@link VoteService#save()}
	 */
	@Override
	public Vote save(Vote vote) {
		return voteRepository.save(vote);
	}

	/**
	 * {@link VoteService#deleteById()}
	 */
	@Override
	public void deleteById(String id) {
		voteRepository.deleteById(id);

	}

}
