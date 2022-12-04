package com.charmingplaces.service;

import java.util.List;
import java.util.Optional;

import com.charmingplaces.entity.Image;


public interface ImageService {
	public List<Image> findAll();

	public Image findById(String id);

	public Image save(String userId, Image image);

	public void deleteById(String id);

	public Optional<Image> update(Image image);

	public Image findByImageId(String imageId);


}
