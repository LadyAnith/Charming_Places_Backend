package com.charmingplaces.service;

import com.charmingplaces.entity.Image;

public interface ImageService {

	/**
	 * Guarda la información de una imagen y la id de usuario para conocer que
	 * usuario ha guardado esa imagen
	 */
	public Image save(String userId, Image image);

	/**
	 * Busca la información de una imagen por su id en la BBDD
	 */
	public Image findByImageId(String imageId);

}
