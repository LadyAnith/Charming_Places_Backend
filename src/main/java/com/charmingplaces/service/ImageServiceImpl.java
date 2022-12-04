package com.charmingplaces.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charmingplaces.entity.Image;
import com.charmingplaces.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	ImageRepository repo;

	@Override
	public List<Image> findAll() {
		return repo.findAll();
	}

	@Override
	public Image findById(String id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Image save(String userId, Image image) {
		image.setImageId(image.getId());
		image.setUserId(userId);

		return repo.save(image);
	}

	@Override
	public void deleteById(String id) {
		repo.deleteById(id);
	}

	@Override
	public Optional<Image> update(Image image) {

		Image oldImage = repo.findById(image.getId()).orElse(null);

		return Optional.ofNullable(oldImage);
	}

	@Override
	public Image findByImageId(String imageId) {
		return repo.findByImageId(imageId).orElse(null);

	}
}
