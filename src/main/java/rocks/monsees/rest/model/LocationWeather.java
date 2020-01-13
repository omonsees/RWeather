package rocks.monsees.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationWeather {

	//TODO implement builder pattern
	
	private Coord coord;
	private Weather[] weather;
	private Main main;
	private String id;
	private String name;
	
	
	
	

}
