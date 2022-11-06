package com.charmingplaces.entity;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Place")
public class Place{
	

	@Id
	private String id; 
	private double xcoord;
	private double ycoord;
	private String hashImagen;
	private String ubicacion;
	private String nombre;
	private String descripcion;
}
