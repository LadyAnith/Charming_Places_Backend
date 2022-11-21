package com.charmingplaces.entity;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Place")
public class Place{
	

	@Id
	private String id; 
	private Location location;
	private String imageId;
	private String address;
	private String city;
	private String name;
}
