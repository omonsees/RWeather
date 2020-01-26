package rocks.monsees.rest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("owm")
@Getter
@Setter
public class OWMConfiguration {

	private String url;
	private String apikey;
	private String units;
	private String iconurl;
	
}
