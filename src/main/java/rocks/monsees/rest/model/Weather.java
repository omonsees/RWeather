package rocks.monsees.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
	
	private int id;
	private String main;
	private String description;
	private String icon;
	
	
}
