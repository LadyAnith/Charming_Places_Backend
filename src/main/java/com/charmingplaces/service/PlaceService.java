package com.charmingplaces.service;

import com.charmingplaces.entity.Place;
import com.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.charmingplaces.pojo.PlacesListResponseDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;


public interface PlaceService {

	/**
	 * Devuelve el listado completo de todos los lugares de interés almacenados en
	 * la BBDD, con su número de votos, si el usuario pasado como parámetro los ha
	 * dado un voto o no. Y la imagen del lugar en Base64.
	 * 
	 * @param userId contiene el id de usuario
	 * @return listado completo de lugares
	 */
	public PlacesListResponseDto findAll(String userId);

	/**
	 * Busca un lugar por su identificador.
	 * 
	 * @param placeId contiene el id del lugar.
	 * @return devuelve un lugar o en el caso de que no exista retornará un null
	 */
	public Place findById(String placeId);

	/**
	 * Devuelve el listado de los lugares que el usuario ha votado
	 * 
	 * @param userId id del usuario
	 * @return un objeto PlacesListResponseDto que contiene el listado de PlacesDto.
	 */
	public PlacesListResponseDto findFavorites(String userId);

	/**
	 * 
	 * Devuelve el listado de los lugares cercanos a la ubicación del usuario
	 * 
	 * @param placesNearRequestDto coordenadas donde se encuentra el usuario
	 * @param userId               id del usuario
	 * @return un objeto PlacesListResponseDto que contiene el listado de PlacesDto.
	 */
	public PlacesListResponseDto findNear(PlacesNearRequestDto placesNearRequestDto, String userId);

	/**
	 * 
	 * Devuelve el listado de los lugares dentro de un área
	 * 
	 * @param request contiene los puntos de un área, el superior izquierdo y el
	 *                inferior derecho
	 * @param userId  id del usuario
	 * @return un objeto PlacesListResponseDto que contiene el listado de PlacesDto.
	 */
	public PlacesListResponseDto findPlacesInsideArea(PlacesInsideAreaRequestDto request, String userId);

	/**
	 * Guarda un lugar en la BBDD
	 * 
	 * @param place información del lugar
	 * @return el lugar insertado
	 */
	public Place save(Place place);

	/**
	 * Actualiza los votos de un lugar
	 * 
	 * @param place información del lugar
	 * @return el lugar actualizado
	 */
	public Place updatePlaceVotes(Place place);

}
