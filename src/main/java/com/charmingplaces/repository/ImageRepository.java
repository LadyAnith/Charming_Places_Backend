package com.charmingplaces.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.charmingplaces.entity.Image;

@Repository
public interface ImageRepository extends MongoRepository <Image, String>{

}
