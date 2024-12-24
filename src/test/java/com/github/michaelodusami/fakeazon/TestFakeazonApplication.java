package com.github.michaelodusami.fakeazon;

import org.springframework.boot.SpringApplication;

public class TestFakeazonApplication {

	public static void main(String[] args) {
		SpringApplication.from(FakeazonApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
