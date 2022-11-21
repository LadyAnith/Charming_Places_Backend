package com.charmingplaces.pojo;

import com.charmingplaces.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageDto {
	
	private int status;
	private boolean success;
	private Image data = new Image();
}
