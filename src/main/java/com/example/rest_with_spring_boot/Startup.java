package com.example.rest_with_spring_boot;

//import java.util.Map;

//import java.util.HashMap;
//import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;


@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);

		/* 
		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", Map.of("pbkdf2", new Pbkdf2PasswordEncoder("", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256)));

		String rawPassword = "admin123";
		String encodedPassword = "{pbkdf2}ef424db56f4df9dc80ee3164c9c3f90ea114ee1011e268eb8609745db9c6e0aeed13ccf0e15e1867";
	
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
		System.out.println("Password matches: " + matches);
		*/

		/* 
		Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
        
        String result = passwordEncoder.encode("admin234");
        System.out.println("My hash " + result);
		String result2 = passwordEncoder.encode("admin123");
        System.out.println("My hash " + result2);
		*/
		
		
	}

}
