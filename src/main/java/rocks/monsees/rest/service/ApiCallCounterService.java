package rocks.monsees.rest.service;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Slf4j
public class ApiCallCounterService {

	private int count;
	private long referenceTime;
	
	
	public ApiCallCounterService() {
		referenceTime = System.currentTimeMillis();
	}
	
	public boolean maximumCallsReached() {
		
		if(timeElapsed()>=60) {
			referenceTime=System.currentTimeMillis();
			count = 0;
			log.debug("API call counter resettet.");
			return false;
		}
		else if (count < 60) {
			count++;
			log.debug("API call count is:"+ count);
			return false;
		}
		log.debug("Maximum API calls reached.");
		return true;
	}
	
	public long timeElapsed() {
		return (System.currentTimeMillis() - referenceTime)/1000;
	}
	
}
