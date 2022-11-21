package com.charmingplaces.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlacesNearResponseDto {
	
	List<PlacesDto> data = new ArrayList<>();

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class PlacesDto {
		private String id;
		private String name;
		private double xcoord;
		private double ycoord;
	}
}
