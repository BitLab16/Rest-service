package site.bitlab16.restservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.controller.TrackedPointController;

@SpringBootTest
class RestServiceApplicationTests {

	//Autowired fa una injection del controller prima che il test sia eseguito
	@Autowired
	private TrackedPointController controller;

	@Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void getAllPoint_thenOK() {

	}

}
