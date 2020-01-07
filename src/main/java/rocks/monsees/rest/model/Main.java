package rocks.monsees.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
	
	private float temp;
	private float pressure;
	private float humidity;
	private float temp_min;
	private float temp_max;
}
