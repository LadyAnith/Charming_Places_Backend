package com.charmingplaces.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageImgurDto {
	
	private String image;
	private String type;
	private String name;
	private String title;
	private String description;

}
