package rocks.monsees.rest.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rocks.monsees.rest.model.LocationWeather;

@Service
@Data
@Slf4j
public class WeatherService {

	private final String appId;
	private final String url;
	private final String units;
	private RestTemplate rt;
	private ApiCallCounterService accs;

	public WeatherService(@Value("${owmApiKey}") String apiKey, @Value("${owmUrl}") String url,
			@Value("${owmUnits}") String units, RestTemplate rt, ApiCallCounterService accs) {
		this.appId = apiKey;
		this.url = url;
		this.units = units;
		this.rt = rt;
		this.accs = accs;
	}

	public Optional<LocationWeather> getWeatherByCityName(String city, String languages) {

		String language = getPrimaryLanguage(languages);

		if (!accs.maximumCallsReached()) {
			//TODO extract as method
			UriComponentsBuilder urib = UriComponentsBuilder.newInstance().scheme("http").host(this.url)
					.path("/data/2.5/weather").queryParam("q", city).queryParam("units", this.units)
					.queryParam("lang", language).queryParam("APPID", this.appId);

			try {
				return Optional.of(rt.getForObject(urib.toUriString(), LocationWeather.class));
			} catch (HttpClientErrorException e) {
				log.info("RestTemplate call raised exception", e);
				return Optional.ofNullable(null);
			}
		}
		return Optional.ofNullable(null);
	}

	private String getPrimaryLanguage(String languages) {
		if (languages.startsWith("de")) {
			return "de";
		}
		return "en";
	}

	public Optional<LocationWeather> getDefaultWeatherByPrimaryLanguage(String languages) {

		String language = getPrimaryLanguage(languages);
		String city;

		if (language.equalsIgnoreCase("de")) {
			city = "Hamburg";
		} else {
			city = "London";
		}

		if (!accs.maximumCallsReached()) {
			UriComponentsBuilder urib = UriComponentsBuilder.newInstance().scheme("http").host(this.url)
					.path("/data/2.5/weather").queryParam("q", city).queryParam("units", this.units)
					.queryParam("lang", language).queryParam("APPID", this.appId);

			try {
				return Optional.of(rt.getForObject(urib.toUriString(), LocationWeather.class));
			} catch (HttpClientErrorException e) {
				log.info("RestTemplate call raised exception", e);
				return Optional.ofNullable(null);
			}
		}
		return Optional.ofNullable(null);

	}

}
