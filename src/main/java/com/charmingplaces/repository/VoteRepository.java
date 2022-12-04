package com.charmingplaces.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.charmingplaces.entity.Vote;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {

	List<Vote> findByPlaces(String placeId);

	Optional<Vote> findByUserIdAndPlaces(String userId, String placeId);

}
