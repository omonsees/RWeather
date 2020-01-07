package rocks.monsees.rest.thcontroller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

	@GetMapping("/error")
	public ModelAndView getErrorPage(HttpServletRequest request, ModelAndView mav) {
		log.info("Houston we have a problem!");

		String msg = "I wanna be your status code";
		Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (statusCode != null) {
			int status = Integer.valueOf(statusCode.toString());
			if (status == HttpStatus.NOT_FOUND.value()) {
				msg = "Ooops, seems like somebody removed your beloved content! :(";
			} else if (status >= 500) {
				msg = "Oops, seems like somebody shut off the server";
			}
		}
		mav.addObject("msg", msg);
		mav.setViewName("error");
		return mav;
	}

	public String getErrorPath() {
		return "/error";
	}

	
	//TODO create a Global Exception Handler calling CustomErrorController on HttpRequestMethodNotSupportedException
	// because otherwise Exceptions outside of spring framework wont be handled (Like Thymeleaf compiler exceptions)
	
}
