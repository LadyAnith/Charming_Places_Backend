package com.charmingplaces.service;

import java.util.List;
import java.util.Optional;

import com.charmingplaces.entity.Vote;


public interface VoteService {

	public Vote findById(String userId);

	public Vote findByUserIdAndPlace(String userId, String placeId);

	public List<Vote> findByPlaceId(String placeId);

	public Vote save(Vote vote);

	public void deleteById(String id);

	public Optional<Vote> update(Vote vote);



}
