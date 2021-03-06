package rocks.monsees.rest.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import rocks.monsees.rest.configuration.OWMConfiguration;
import rocks.monsees.rest.model.LocationWeather;
import rocks.monsees.rest.service.WeatherService;

@Slf4j
@Controller
@RequestMapping("/weather")
public class WeatherController {

	private WeatherService weatherService;
	private OWMConfiguration owmConfig;

	public WeatherController(WeatherService weatherService, OWMConfiguration owmConfig) {
		super();
		this.weatherService = weatherService;
		this.owmConfig=owmConfig;
	}

	@GetMapping
	public ModelAndView getWeatherPage(HttpServletRequest request, ModelAndView mav) {
		
		String languages = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		Optional<LocationWeather> lw = weatherService.getDefaultWeatherByPrimaryLanguage(languages);

		if (lw.isPresent()) {
			mav.addObject("lweather", lw.get());
			mav.addObject("iconUrl", getWeatherIconUrl(lw.get().getWeather()[0].getIcon()));
		} else {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
		}

		mav.setViewName("weather");
		return mav;
	}

	@PostMapping
	public ModelAndView getWeatherPageWithLocationWeather(@RequestParam String location, HttpServletRequest request,
			ModelAndView mav) {

		String languages = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		Optional<LocationWeather> lw = weatherService.getWeatherByCityName(location.trim(), languages);
		if (lw.isPresent()) {
			mav.addObject("lweather", lw.get());
			mav.addObject("iconUrl", getWeatherIconUrl(lw.get().getWeather()[0].getIcon()));
		} else {
			// TODO add toast message if location not found
			Optional<LocationWeather> lwDefault = weatherService.getDefaultWeatherByPrimaryLanguage(languages);
			if (lwDefault.isPresent()) {
				mav.addObject("lweather", lwDefault.get());
				mav.addObject("iconUrl", getWeatherIconUrl(lwDefault.get().getWeather()[0].getIcon()));
				mav.addObject("unknownLocation", location);
			} else {
				throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
			}
		}

		mav.setViewName("weather");
		return mav;
	}

	private String getWeatherIconUrl(String iconId) {
		return owmConfig.getIconurl() + iconId + "@2x.png";
	}

}
