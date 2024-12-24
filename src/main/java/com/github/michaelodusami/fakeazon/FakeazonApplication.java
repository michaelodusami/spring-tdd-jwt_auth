package com.github.michaelodusami.fakeazon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class FakeazonApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakeazonApplication.class, args);
	}

}
