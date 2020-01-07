package rocks.monsees.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RWeatherApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(RWeatherApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(PostService ps) {
//		return args -> {
//			ps.getPostWithComments(1l);
//		};
//	}
	
	
//	@Bean
//	CommandLineRunner runner(WeatherService ws) {
//
//		return args -> {
//			ws.getWeatherByCityName("Bremen");
//		};
//	}

	
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder rtb) {
		return rtb.build();
	}

}
