package com.charmingplaces.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Clase que guarda las coordenadas de uno o varios puntos
 * 
 * @author Ana María Ramírez Dorado
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

	private String type = "Point";
	private List<Double> coordinates;

}
