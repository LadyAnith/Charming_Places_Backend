package com.charmingplaces.service;

import com.charmingplaces.pojo.CreatePlaceRequestDto;

public interface ImageUploadService {

	/**
	 * Este método manda la imagen a Imgur
	 * 
	 * Guarda la información de la imagen de Imgur y el usuario que ha realizado la
	 * captura en la BBDD
	 * 
	 * Guarda en BBDD un nuevo lugar con el id de usuario, guarda el id de la
	 * información de la imagen guardada en la BBDD y la información de la captura
	 * 
	 * 
	 * @param userId  id del usuario
	 * @param capture información de la captura
	 */
	public void uploadImage(String userId, CreatePlaceRequestDto capture);
}
