package com.charmingplaces.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlaceRequestDto{
	private String name;
	private String city;
	private String address;	
	private double xcoord;
	private double ycoord;
	private byte[] image;
}
