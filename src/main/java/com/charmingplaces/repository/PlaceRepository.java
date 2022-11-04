package com.charmingplaces.repository;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.charmingplaces.entity.Place;


@Repository
public interface PlaceRepository extends MongoRepository <Place, String>{

}
