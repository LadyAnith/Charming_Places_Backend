package com.charmingplaces.service;

import java.util.List;

import com.charmingplaces.entity.Vote;


public interface VoteService {

	/**
	 * Busca un voto por el identificador del usuario.
	 * 
	 * @param userId contiene el id de usuario
	 * @return devuelve un voto o en el caso de que no exista un usuario con votos
	 *         retornará un null
	 */
	public Vote findById(String userId);

	/**
	 * Busca un voto por el identificador del usuario y por el id de lugar si existe
	 * entre los que el usuario ha votado.
	 * 
	 * 
	 * @param userId  contiene el id de usuario
	 * @param placeId id del lugar
	 * @return devuelve un voto o en el caso de que no exista un usuario con votos o
	 *         que el usuario no haya votado ese lugar retornará un null
	 */
	public Vote findByUserIdAndPlaces(String userId, String placeId);

	/**
	 * Busca en el listado de votos de todos los usuarios que el lugar exista entre
	 * sus lugares votados.
	 * 
	 * @param placeId id de lugar
	 * @return retorna un listado de votos que contengan ese lugar.
	 */
	public List<Vote> findByPlaceId(String placeId);

	/**
	 * Guarda un nuevo voto o lo actualiza.
	 * 
	 * @param vote le mandamos como parámetro la información del voto.
	 * @return devuelve el voto con la nueva información.
	 */
	public Vote save(Vote vote);

	/**
	 * Elimina un voto por su id.
	 * 
	 * @param id como parámetro se le manda el id del voto.
	 */
	public void deleteById(String id);



}
