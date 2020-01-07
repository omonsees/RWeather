package rocks.monsees.rest.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ApiCallCounterServiceTest {

	ApiCallCounterService accs;
	
	@BeforeEach
	void setUp() throws Exception {
		accs = new ApiCallCounterService();
	}

	@Test
	void test() throws InterruptedException {
		int i =0;
		while(i<62) {
			log.error("Maximum reached? "+accs.maximumCallsReached());
			log.error(""+accs.getReferenceTime());
			log.error("Seconds elapsed: "+accs.timeElapsed());
			log.error("Count is: "+i);
			i++;
			Thread.sleep(500);
			
		}
		assertEquals(false, accs.maximumCallsReached());
	
	}

}
