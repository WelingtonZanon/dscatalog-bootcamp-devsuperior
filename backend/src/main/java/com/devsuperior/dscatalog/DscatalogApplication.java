package com.devsuperior.dscatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DscatalogApplication /* implements CommandLineRunner */{

	
	/*
	 * @Autowired private S3Service s3Service;
	 */
	
	
	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

	/*teste S3 com app local
	 * 
	 * @Override public void run(String... args) throws Exception {
	 * s3Service.uploadFile("c:\\temp\\teste.png"); }
	 */

}
