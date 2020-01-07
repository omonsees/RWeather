package rocks.monsees.rest.thcontroller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import rocks.monsees.rest.model.LocationWeather;
import rocks.monsees.rest.service.WeatherService;

@Slf4j
@Controller
@RequestMapping("/weather")
public class WeatherController {

	private WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		super();
		this.weatherService = weatherService;	
	}

	@GetMapping
	public ModelAndView getWeatherPage(HttpServletRequest request,ModelAndView mav) {
		
		String languages = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		Optional<LocationWeather> lw = weatherService.getDefaultWeatherByPrimaryLanguage(languages);
		//TODO change button in weather html to be i18n
		if (lw.isPresent()) {
			mav.addObject("lweather", lw.get());
		} else {
			mav.addObject("notFound", "default Location"); 
		}
		
		mav.setViewName("weather");
		return mav;
	}

	@PostMapping
	public ModelAndView getWeatherPageWithLocationWeather(@RequestParam String location, HttpServletRequest request,ModelAndView mav) {

		String languages = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		Optional<LocationWeather> lw = weatherService.getWeatherByCityName(location.trim(),languages);
		if (lw.isPresent()) {
			mav.addObject("lweather", lw.get());
		} else {
			mav.addObject("notFound", location); 
		}
		
		mav.setViewName("weather");
		return mav;
	}

}
