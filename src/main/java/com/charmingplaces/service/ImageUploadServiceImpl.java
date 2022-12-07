package com.charmingplaces.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.charmingplaces.entity.Image;
import com.charmingplaces.entity.Location;
import com.charmingplaces.entity.Place;
import com.charmingplaces.pojo.CreatePlaceRequestDto;
import com.charmingplaces.pojo.ImageDto;
import com.charmingplaces.pojo.ImageImgurDto;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImageUploadServiceImpl.class);

	private String imgurClientID = "Client-ID 26ade6c10d030fc";

	@Autowired
	ImageService imageService;

	@Autowired
	PlaceService placeService;

	/**
	 * {@link ImageUploadService#uploadImage()}
	 */
	@Override
	public void uploadImage(String userId, CreatePlaceRequestDto capture) {
		ImageDto imageDataFromImgur = createImageInImgur(capture);

		Image image = saveImageInBBDD(userId, imageDataFromImgur);
		
		savePlaceInBBDD(userId, capture, image);
		
	}

	// METODOS AUXILIARES

	/**
	 * Este método crea una imagen en imgur.
	 * 
	 * Convierte la imagen en Base64 y manda la información que nos pide imgur para
	 * guardar la imagen.
	 * 
	 * 
	 * 
	 * @param capture captura realiza por el usuario
	 * @return respuesta de imgur
	 */
	private ImageDto createImageInImgur(CreatePlaceRequestDto capture) {
		// Esta es la URL a donde vamos a mandar o consultar datos, en este caso la de
		// imgur para subir fotos
		String url = "https://api.imgur.com/3/upload";

		// Este es el cuerpo que vamos a mandar a la api
		ImageImgurDto imageImgurDto = ImageImgurDto.builder()
				.image(Base64Utils.encodeToString(capture.getImage()))
			.description("")
			.name(UUID.randomUUID() +  ".jpg")
				.title(capture.getName())
			.type("base64")
			.build();

		// estas son las cabeceras, dado que vamos a subir contenido y requiere
		// autenticación pues:
		// - el authorization es la autenticación
		// - el contentype es el tipo de dato que mandamos, en nuestro caso un objeto
		// que spring convertirá en json por nosotros sin tner que hacer nada mas
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", imgurClientID);

		// la request es la suma de las cabeceras + el body, nuesto body es el objeto
		// que mandaremos a imgur, y las cabeceras ya las tenemos
		HttpEntity<ImageImgurDto> request = new HttpEntity<>(imageImgurDto, headers);

		// esta es la clase donde voy almacenar la respuesta que me mandará la api
		Class<ImageDto> responseClass = ImageDto.class;

		// Con rest template hago una petición post mandando la información y capturo la
		// respuesta
		RestTemplate restTemplate = new RestTemplate();
		ImageDto response = restTemplate.postForObject(url, request, responseClass);
		// en response están los datos de la imagen que hemos guardado en imgur

		LOG.debug("response: {}", response);
		return Optional.ofNullable(response).orElseThrow(() -> new RuntimeException("No se pudo recuperar la imagen"));
	}

	/**
	 * Guarda la información de la imagen de imgur y el id del usuario en BBDD
	 * 
	 * @param userId         id del usuario
	 * @param imgDtoResponse respuesta de la api imgur
	 * @return información guardada de imgur en la BBDD
	 */
	private Image saveImageInBBDD(String userId, ImageDto imgDtoResponse) {
		// almaceno la referencia de la imagen de imgur en bbdd y retorno su objeto
		// persistido
		return imageService.save(userId, imgDtoResponse.getData());
	}

	/**
	 * Guarda el nuevo lugar en la BBDD, con el id de la imagen, la información de
	 * la captura y el id del usuario que ha hecho la captura, además guarda las
	 * coordenadas de la captura donde se ha realizado la foto
	 * 
	 * @param userId  id del usuario
	 * @param capture información de la captura
	 * @param image   información de la imagen de imgurl en la BBDD
	 */
	private void savePlaceInBBDD(String userId, CreatePlaceRequestDto capture, Image image) {
		Place place = Place.builder()
				.imageId(image.getImageId())
				.name(capture.getName()).city(capture.getCity()).address(capture.getAddress())
				.userId(userId).build();

		Location location = new Location();
		location.setCoordinates(Arrays.asList(capture.getXcoord(), capture.getYcoord()));
		place.setLocation(location);

		placeService.save(place);
	}
}
