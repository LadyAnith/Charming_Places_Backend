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
public class PlacesListResponseDto {

	List<PlacesDto> data = new ArrayList<>();

}
