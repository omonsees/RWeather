package rocks.monsees.rest.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rocks.monsees.rest.configuration.OWMConfiguration;
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
	private OWMConfiguration owmConfig;

	public WeatherService(RestTemplate rt, ApiCallCounterService accs, OWMConfiguration owmConfig) {
		this.appId = owmConfig.getApikey();
		this.url = owmConfig.getUrl();
		this.units = owmConfig.getUnits();
		this.rt = rt;
		this.accs = accs;
	}

	public Optional<LocationWeather> getWeatherByCityName(String cityWithUmlauts, String languages) {

		String city = removeUmlautsFromCityString(cityWithUmlauts);
		String language = getPrimaryLanguage(languages);

		if (!accs.maximumCallsReached()) {

			String url = getUrlForRestCall(language, city);

			try {
				return Optional.of(rt.getForObject(url, LocationWeather.class));
			} catch (HttpClientErrorException e) {
				return Optional.ofNullable(null);
			}
		}
		return Optional.ofNullable(null);
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

			String url = getUrlForRestCall(language, city);

			try {
				return Optional.of(rt.getForObject(url, LocationWeather.class));
			} catch (HttpClientErrorException e) {
				log.info("RestTemplate call raised exception", e);
				return Optional.ofNullable(null);
			}
		}
		return Optional.ofNullable(null);

	}

	private String getUrlForRestCall(String language, String cityWithUmlauts) {
		
		String city = removeUmlautsFromCityString(cityWithUmlauts);
		
		UriComponentsBuilder urib = UriComponentsBuilder.newInstance().scheme("http").host(this.url)
				.path("/data/2.5/weather").queryParam("q", city).queryParam("units", this.units)
				.queryParam("lang", language).queryParam("APPID", this.appId);

		log.debug("Uri: " + urib.toUriString());
		return urib.toUriString();

	}

	private String getPrimaryLanguage(String languages) {
		if (languages.startsWith("de")) {
			return "de";
		}
		return "en";
	}

	private String removeUmlautsFromCityString(String city) {
		String[][] umlaute = new String[][] { { "ä", "ae" }, { "ö", "oe" }, { "ü", "ue" } };

		for (int i = 0; i < umlaute.length; i++) {
			city = city.replace(umlaute[i][0], umlaute[i][1]);
		}

		return city;
	}

}
