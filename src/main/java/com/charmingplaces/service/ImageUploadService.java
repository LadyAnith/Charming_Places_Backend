package com.charmingplaces.service;

import com.charmingplaces.pojo.CreatePlaceRequestDto;

public interface ImageUploadService {

	public CreatePlaceRequestDto uploadImage(String userId, CreatePlaceRequestDto photo);
}
