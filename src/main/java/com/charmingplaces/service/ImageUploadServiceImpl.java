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

	@Override
	public CreatePlaceRequestDto uploadImage(String userId, CreatePlaceRequestDto photo) {
		ImageDto imageDataFromImgur = createImageInImgur(photo);

		Image image = persistImage(userId, imageDataFromImgur);
		
		persistPlace(userId, photo, image);
		
		return photo;

	}

	private ImageDto createImageInImgur(CreatePlaceRequestDto photo) {
		// Esta es la URL a donde vamos a mandar o consultar datos, en este caso la de
		// imgur para subir fotos
		String url = "https://api.imgur.com/3/upload";

		// Este es el cuerpo que vamos a mandar a la api
		ImageImgurDto imageImgurDto = ImageImgurDto.builder()
			.image(Base64Utils.encodeToString(photo.getImage()))
			.description("")
			.name(UUID.randomUUID() +  ".jpg")
			.title(photo.getName())
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

	private Image persistImage(String userId, ImageDto imgDtoResponse) {
		// almaceno la referencia de la imagen de imgur en bbdd y retorno su objeto
		// persistido
		return imageService.save(userId, imgDtoResponse.getData());
	}

	private void persistPlace(String userId, CreatePlaceRequestDto photo, Image image) {
		Place place = Place.builder()
				.imageId(image.getImageId())
				.name(photo.getName())
				.city(photo.getCity())
				.address(photo.getAddress())
				.userId(userId).build();

		Location location = new Location();
		location.setCoordinates(Arrays.asList(photo.getXcoord(), photo.getYcoord()));
		place.setLocation(location);

		placeService.save(place);
	}
}
