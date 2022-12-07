package com.charmingplaces.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que guarda los lugares votados por un usuario
 * 
 * @author Ana María Ramírez Dorado
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Vote")
public class Vote {

	@Id
	private String userId;
	@Builder.Default
	private Set<String> places = new HashSet<>();
}
