package com.charmingplaces.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto{
	private double xcoord;
	private double ycoord;
	private byte[] image;
}
