package com.charmingplaces.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo{
	private double xcoord;
	private double ycoord;
	private byte[] image;
}
