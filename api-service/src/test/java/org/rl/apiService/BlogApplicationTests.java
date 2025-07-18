package org.rl.apiService;

import org.junit.jupiter.api.Test;
import org.rl.apiService.utils.PostgreSqlTestContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
class BlogApplicationTests implements PostgreSqlTestContainer {

	@Test
	void contextLoads() {
	}

}
