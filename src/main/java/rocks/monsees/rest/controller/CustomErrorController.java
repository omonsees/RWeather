package rocks.monsees.rest.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
// Error controller is the base class (global error controller) catching everything not already catched 
// can be preceded by @ControllerAdvice if useful (spring wraps unhandled custom exceptions with
// NestedSevletExcption an Status Code 500. So @ControllerAdvice can be used to catch those an add a proper
// response code or whatever
// https://stackoverflow.com/questions/55101797/using-spring-boots-errorcontroller-and-springs-responseentityexceptionhandler
public class CustomErrorController implements ErrorController {

	// could also be in a separate controller
	@GetMapping("/error")
	public ModelAndView getErrorPage(HttpServletRequest request, ModelAndView mav) {
		log.info("Houston we have a problem!");
		String error = "500";
		Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		// to get base class of exception when Spring throws a "NestedServletExcption"
		// will return null if not a nested exception
		Object exception = request.getAttribute("javax.servlet.error.exception");
		if (String.valueOf(exception) != null) {
			log.info("Nested Exception: " + String.valueOf(exception));
		}

		// add status code to model for proper message rendering on template
		// does it ever become null? Cause Spring is wrapping unhandled exceptions
		//TODO add switch statement for other errors
		if (statusCode != null) {
			int status = Integer.valueOf(statusCode.toString());
			if (status == HttpStatus.NOT_FOUND.value()) {
				// TODO use i18n for error message
				 error = "404";
			} else if (status >= 500) {
				error = "500";
			}
		}
		mav.addObject("error", error);
		mav.setViewName("error");
		return mav;
	}

	// if not overridden spring boot will not look for a controller with
	// "/error" mapping. In fact it will just uses its BasicErrorController, which
	// will
	// delegate to error.html. Consequence is that no model data can be added like
	// above
	// if overridden then a Controller with mapping for "/error" has to be provided
	@Override
	public String getErrorPath() {
		return "/error";
	}

}
