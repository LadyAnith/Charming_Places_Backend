package com.charmingplaces.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlacesInsideAreaRequestDto {
	private GeoPoint geoPointTopLeft;
	private GeoPoint geoPointBottomRight;
}
