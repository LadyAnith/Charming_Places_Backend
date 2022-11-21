package com.charmingplaces.entity;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Image")
public class Image {
	@Id
	private String _id;
	private String id;
	private String deletehash;
	private String description;
	private String title;
	private String name;
	private String type;
	private int width;
	private int heigh;
	private int size;
	private String link;
	//TODO esto no va fino llegan numeros de la api de imgur hay que transformarlo a date
	private Timestamp datetime;

}
