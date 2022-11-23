package com.charmingplaces.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlacesDto {
	private String id;
	private String name;
	private String url;
	private double xcoord;
	private double ycoord;
}