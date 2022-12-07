package com.charmingplaces.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Image;
import com.charmingplaces.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	ImageRepository repo;

	/**
	 * {@link ImageService#save()}
	 */
	@Override
	public Image save(String userId, Image image) {
		image.setImageId(image.getId());
		image.setUserId(userId);

		return repo.save(image);
	}

	/**
	 * {@link ImageService#findByImageId()}
	 */
	@Override
	public Image findByImageId(String imageId) {
		return repo.findByImageId(imageId).orElse(null);

	}
}
