package io.github.gabrielwederson.help_desk_pro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableCaching
@SpringBootApplication
public class HelpDeskProApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskProApplication.class, args);
	}

}
