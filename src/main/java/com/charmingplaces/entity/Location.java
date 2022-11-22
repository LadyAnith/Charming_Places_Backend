package com.charmingplaces.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

	private String type = "Point";
	private List<Double> coordinates;

}
