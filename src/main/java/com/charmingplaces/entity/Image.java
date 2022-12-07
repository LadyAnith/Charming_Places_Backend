package com.charmingplaces.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Clase que guarda la información relevante de una imagen añadida en Imgur
 * 
 * @author Ana María Ramírez Dorado
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Image")
public class Image {
	@Id
	private String id;
	private String imageId;
	private String deletehash;
	private String description;
	private String title;
	private String name;
	private String type;
	private int width;
	private int heigh;
	private int size;
	private String link;
	private Integer datetime;
	private String userId;

}
