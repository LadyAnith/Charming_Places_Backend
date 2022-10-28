package com.charmingplaces.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceController {
	
	@GetMapping("/")
	public String testing() {
		return "Probando, probando 1, 2 y 3!!";
	}

}
