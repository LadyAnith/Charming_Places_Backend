package com.charmingplaces.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charmingplaces.pojo.CreatePlaceRequestDto;
import com.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.charmingplaces.pojo.PlacesListResponseDto;
import com.charmingplaces.pojo.PlacesNearRequestDto;
import com.charmingplaces.service.ImageUploadService;
import com.charmingplaces.service.PlaceService;

/**
 * <p>
 * Esta clase se encarga de gestionar peticiones referentes a los lugares de
 * interés.
 * </p>
 * 
 * <p>
 * Esta clase contiene cinco endpoint.
 * </p>
 * <ul>
 * <li>Listar todos los lugares.</li>
 * <li>Listar los favoritos de un usuario.</li>
 * <li>Buscar los lugares cercanos a unas coordenadas.</li>
 * <li>Crear lugar de interés.</li>
 * <li>Buscar dentro de un área.</li>
 * </ul>
 * 
 * @author Ana María Ramírez Dorado
 *
 */
@RestController
@RequestMapping("/places")
public class PlaceController {

	private static final Logger LOG = LoggerFactory.getLogger(PlaceController.class);

	private static final String USER_ID = "user-id";

	@Autowired
	private PlaceService service;

	@Autowired
	private ImageUploadService imageUploadService;
	
	/**
	 * Método que busca el listado completo de los lugares de interés. Utiliza el id
	 * del usuario que recupera de las cabeceras para indicar la información
	 * adicional sobre qué lugares han sido votados por el usuario.
	 * 
	 * @param headers contiene el id de usuario
	 * @return listado de lugares
	 */
	@GetMapping
	public ResponseEntity<PlacesListResponseDto> findAll(@RequestHeader Map<String, String> headers) {
		String userId = headers.get(USER_ID);
		return ResponseEntity.ok(service.findAll(userId));
	}

	/**
	 * Busca el listado completo de lugares favoritos del usuario. Recupera el id de
	 * las cabeceras para buscar entre los votos, los lugares que han sido votados
	 * por el usuario.
	 * 
	 * @param headers contiene el id de usuario
	 * @return listado de lugares favoritos
	 */
	@GetMapping("/findFavorites")
	public ResponseEntity<PlacesListResponseDto> findFavorites(@RequestHeader Map<String, String> headers) {
		String userId = headers.get(USER_ID);
		return ResponseEntity.ok(service.findFavorites(userId));
	}

	/**
	 * Encuentra el listado de lugares de interés cerca de la ubicación del usuario.
	 * 
	 * 
	 * @param headers              contiene el id de usuario
	 * @param placesNearRequestDto Clase que guarda la coordenada X y la coordenada
	 *                             Y del usuario para buscar los lugares que hay en
	 *                             un área (Circunferencia) concreta.
	 * @return listado de lugares dentro de ese área.
	 */
	@GetMapping("/findNear")
	public ResponseEntity<PlacesListResponseDto> findNear(@RequestHeader Map<String, String> headers,
			PlacesNearRequestDto placesNearRequestDto) {
		String userId = headers.get(USER_ID);

		PlacesListResponseDto result = service.findNear(placesNearRequestDto, userId);
		
		return ResponseEntity.ok(result);

	}
	
	/**
	 * Crea un nuevo lugar de interés. Se apoya en la clase CreatePlaceRequestDto,
	 * para guardar la información del lugar que se va a agregar. Sus datos
	 * almacenados son:
	 * 
	 * El name: Es el nombre que ha especificado el usuario en el momento de
	 * capturar el lugar.
	 * 
	 * City y address: guarda la ciudad y dirección del lugar que se va a crear
	 * optenidos por GPS.
	 * 
	 * xcoord e ycoord: guarda la coordenada X y la coordenada Y del lugar donde ha
	 * tomado la captura.
	 * 
	 * Image: guarda la imagen en un bitmap que posteriormente se convertirá en
	 * Base64 para mandarla al imgur.
	 * 
	 * 
	 * @param headers contiene el id de usuario
	 * @param request Información para crear el lugar de interés.
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> create(@RequestHeader Map<String, String> headers,
			@RequestBody CreatePlaceRequestDto request) {
		LOG.info("ENTRANDO en addPhoto request : {}", request);

		
		String userId = headers.get(USER_ID);
		imageUploadService.uploadImage(userId, request);

		return ResponseEntity.ok(null);
	}

	/**
	 * Este método se encarga de buscar todos los lugares que hay dentro de un área.
	 * Este área se establece entornos a dos puntos de la pantalla, tomando como
	 * referencia el mapa visible en el momento de haccer la petición.
	 * 
	 * @param headers headers contiene el id de usuario.
	 * @param request contiene dos geopoints, el de la esquina superior izquierda de
	 *                la pantalla y el de la esquina inferior derecha.
	 * 
	 * @return retorna un listado con los lugares dentro de ese área.
	 */
	@PostMapping(value = "/placesInsideArea")
	public ResponseEntity<PlacesListResponseDto> findPlacesInsideArea(@RequestHeader Map<String, String> headers,
			@RequestBody PlacesInsideAreaRequestDto request) {
		String userId = headers.get(USER_ID);
		LOG.info("ENTRANDO en findPlacesInsideArea request : {}", request);
		PlacesListResponseDto result = service.findPlacesInsideArea(request, userId);
		return ResponseEntity.ok(result);
	}



}
